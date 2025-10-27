package com.blurr.voice.data

import android.content.Context
import android.util.Log
import com.blurr.voice.MyApplication
import com.blurr.voice.managers.PuterManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Manager class for handling memory operations with puter.js
 */
class MemoryManager(private val context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val memoryDao = database.memoryDao()
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    /**
     * Add a new memory, checking for duplicates first
     */
    suspend fun addMemory(originalText: String, checkDuplicates: Boolean = true): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("MemoryManager", "Adding memory: ${originalText.take(100)}...")
                
                if (checkDuplicates) {
                    // Check for similar existing memories first
                    val similarMemories = findSimilarMemories(originalText, similarityThreshold = 0.85f)
                    if (similarMemories.isNotEmpty()) {
                        Log.d("MemoryManager", "Found ${similarMemories.size} similar memories, skipping duplicate")
                        return@withContext true // Return true since we're avoiding a duplicate
                    }
                }
                
                // Create memory entity without embedding for puter.js compatibility
                val memory = Memory(
                    originalText = originalText,
                    embedding = "[]" // Empty embedding array as placeholder
                )
                
                // Save to database
                val id = memoryDao.insertMemory(memory)
                Log.d("MemoryManager", "Successfully added memory with ID: $id")
                
                // Save to puter.js key-value store if user is signed in
                if (PuterManager.getInstance(context).isUserSignedIn()) {
                    val memoryKey = "memory_${System.currentTimeMillis()}"
                    val memoryData = mapOf(
                        "originalText" to originalText,
                        "timestamp" to System.currentTimeMillis().toString(),
                        "id" to id.toString()
                    )
                    PuterManager.getInstance(context).saveMessageToKvStore(memoryKey, memoryData)
                }
                
                return@withContext true
                
            } catch (e: Exception) {
                Log.e("MemoryManager", "Error adding memory $e", e)
                return@withContext false
            }
        }
    }

    /**
     * Fire-and-forget version of addMemory that is not tied to an Activity scope.
     * Uses an internal SupervisorJob so it won't be cancelled when a caller finishes.
     */
    fun addMemoryFireAndForget(originalText: String, checkDuplicates: Boolean = true) {
        ioScope.launch {
            try {
                val result = addMemory(originalText, checkDuplicates)
                Log.d("MemoryManager", "Fire-and-forget addMemory result=$result")
            } catch (e: Exception) {
                Log.e("MemoryManager", "Fire-and-forget addMemory error", e)
            }
        }
    }
    
    /**
     * Search for relevant memories based on a query
     */
    suspend fun searchMemories(query: String, topK: Int = 3): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("MemoryManager", "Searching memories for query: ${query.take(100)}...")
                
                // Get all memories from database
                val allMemories = memoryDao.getAllMemoriesList()
                
                if (allMemories.isEmpty()) {
                    Log.d("MemoryManager", "No memories found in database")
                    return@withContext emptyList()
                }
                
                // Simple text-based similarity (substring matching) instead of embeddings
                val similarities = allMemories.map { memory ->
                    val similarity = calculateTextSimilarity(query.lowercase(), memory.originalText.lowercase())
                    Pair(memory.originalText, similarity)
                }.sortedByDescending { it.second }
                
                // Return top K memories
                val topMemories = similarities.take(topK).map { it.first }
                Log.d("MemoryManager", "Found ${topMemories.size} relevant memories")
                return@withContext topMemories
                
            } catch (e: Exception) {
                Log.e("MemoryManager", "Error searching memories", e)
                return@withContext emptyList()
            }
        }
    }
    
    /**
     * Get relevant memories for a task and format them for prompt augmentation
     */
    suspend fun getRelevantMemories(taskDescription: String): String {
        val relevantMemories = searchMemories(taskDescription, topK = 3)
        
        return if (relevantMemories.isNotEmpty()) {
            buildString {
                appendLine("--- Relevant Information ---")
                relevantMemories.forEach { memory ->
                    appendLine("- $memory")
                }
                appendLine()
                appendLine("--- My Task ---")
                appendLine(taskDescription)
            }
        } else {
            // If no relevant memories, just return the original task
            taskDescription
        }
    }
    
    /**
     * Get memory count
     */
    suspend fun getMemoryCount(): Int {
        return withContext(Dispatchers.IO) {
            memoryDao.getMemoryCount()
        }
    }
    
    /**
     * Get all memories as a list
     */
    suspend fun getAllMemoriesList(): List<Memory> {
        return withContext(Dispatchers.IO) {
            memoryDao.getAllMemoriesList()
        }
    }
    
    /**
     * Delete all memories
     */
    suspend fun clearAllMemories() {
        withContext(Dispatchers.IO) {
            memoryDao.deleteAllMemories()
            Log.d("MemoryManager", "All memories cleared")
        }
    }
    
    /**
     * Delete a specific memory by ID
     */
    suspend fun deleteMemoryById(id: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                memoryDao.deleteMemoryById(id)
                Log.d("MemoryManager", "Successfully deleted memory with ID: $id")
                true
            } catch (e: Exception) {
                Log.e("MemoryManager", "Error deleting memory with ID: $id", e)
                false
            }
        }
    }
    
    /**
     * Find memories similar to the given text
     */
    suspend fun findSimilarMemories(text: String, similarityThreshold: Float = 0.8f): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                // Get all memories from database
                val allMemories = memoryDao.getAllMemoriesList()
                
                if (allMemories.isEmpty()) {
                    return@withContext emptyList()
                }
                
                // Calculate similarities and find similar memories using simple text matching
                val similarMemories = allMemories.mapNotNull { memory ->
                    val similarity = calculateTextSimilarity(text.lowercase(), memory.originalText.lowercase())
                    
                    if (similarity >= similarityThreshold) {
                        Log.d("MemoryManager", "Found similar memory (similarity: $similarity): ${memory.originalText.take(50)}...")
                        memory.originalText
                    } else {
                        null
                    }
                }
                
                Log.d("MemoryManager", "Found ${similarMemories.size} similar memories with threshold $similarityThreshold")
                return@withContext similarMemories
                
            } catch (e: Exception) {
                Log.e("MemoryManager", "Error finding similar memories", e)
                return@withContext emptyList()
            }
        }
    }
    
    /**
     * Calculate simple text similarity using common substring approach
     */
    private fun calculateTextSimilarity(text1: String, text2: String): Float {
        if (text1.isEmpty() && text2.isEmpty()) return 1.0f
        if (text1.isEmpty() || text2.isEmpty()) return 0.0f
        
        // Simple approach: calculate similarity based on common words
        val words1 = text1.split("\\s+".toRegex()).filter { it.isNotEmpty() }
        val words2 = text2.split("\\s+".toRegex()).filter { it.isNotEmpty() }
        
        if (words1.isEmpty() && words2.isEmpty()) return 1.0f
        if (words1.isEmpty() || words2.isEmpty()) return 0.0f
        
        val commonWords = words1.intersect(words2.toSet()).size
        val totalWords = maxOf(words1.size, words2.size)
        
        return if (totalWords > 0) commonWords.toFloat() / totalWords else 0.0f
    }
    
    companion object {
        private var instance: MemoryManager? = null
        
        fun getInstance(context: Context = MyApplication.appContext): MemoryManager {
            return instance ?: synchronized(this) {
                instance ?: MemoryManager(context).also { instance = it }
            }
        }
    }
}