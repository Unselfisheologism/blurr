package com.blurr.voice.data

import android.util.Log
//import com.blurr.voice.api.GeminiApi
// REMOVED: import com.google.ai.client.generativeai.type.TextPart  // Removed Google Generative AI dependency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.blurr.voice.managers.PuterManager
import android.content.Context

/**
 * Extracts and stores memories from conversations using LLM-based memory extraction
 */
class MemoryExtractor(private val context: Context) {
    
    private val memoryExtractionPrompt = """
        You are a memory extraction agent. Analyze the following conversation and extract key, lasting facts about the user which are supposed to be known by perfect friend to understand the user better.
        
        Focus on:
        - Personal details (family, relationships, preferences, life events)
        - Significant experiences or traumas or hobbies
        - Important dates, locations, or circumstances
        - Long-term preferences or goals or habits etc
        Ignore:
        - Fleeting emotions or temporary states
        - Generic statements or hypothetical scenarios
        - Technical details or app-specific information
        
        IMPORTANT: Do NOT extract memories that are semantically equivalent to the following already known memories:
        {used_memories}
        
        Format each memory as a clear, concise sentence that captures the essential fact.
        If no significant memories are found, return "NO_MEMORIES".
        
        Conversation:
        {conversation}
        
        Extracted Memories (one per line):
    """.trimIndent()
    
    /**
     * Extracts memories from a conversation and stores them asynchronously
     * This is a fire-and-forget operation that doesn't block the conversation flow
     * 
     * @param conversationHistory The conversation to extract memories from
     * @param memoryManager The memory manager instance
     * @param usedMemories Set of memories already used in this conversation to avoid duplicates
     */
    suspend fun extractAndStoreMemories(
        conversationHistory: List<Pair<String, List<String>>>,
        memoryManager: MemoryManager,
        usedMemories: Set<String> = emptySet()
    ) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("MemoryExtractor", "Starting memory extraction from conversation")
                Log.d("MemoryExtractor", "Used memories count: ${usedMemories.size}")

                // Convert conversation to text format for analysis
                val conversationText = formatConversationForExtraction(conversationHistory)

                // Format used memories for the prompt
                val usedMemoriesText = if (usedMemories.isNotEmpty()) {
                    usedMemories.joinToString("\n") { "- $it" }
                } else {
                    "None"
                }

                // Create the extraction prompt with used memories
                val extractionPrompt = memoryExtractionPrompt
                    .replace("{conversation}", conversationText)
                    .replace("{used_memories}", usedMemoriesText)

                // Call Puter.js for memory extraction
                val puterManager = PuterManager.getInstance(context)
                val extractionResponse = puterManager.chatWithAI(extractionPrompt)

                if (extractionResponse != null) {
                    Log.d("MemoryExtractor", "Memory extraction response: ${extractionResponse.take(200)}...")

                    // Parse the extracted memories
                    val memories = parseExtractedMemories(extractionResponse)

                    if (memories.isNotEmpty()) {
                        Log.d("MemoryExtractor", "Extracted ${memories.size} memories")

                        // Store each memory asynchronously (no need for string filtering since LLM handles it)
                        memories.forEach { memory ->
                            try {
                                val success = memoryManager.addMemory(memory)
                                if (success) {
                                    Log.d("MemoryExtractor", "Successfully stored memory: $memory")
                                } else {
                                    Log.e("MemoryExtractor", "Failed to store memory: $memory")
                                }
                            } catch (e: Exception) {
                                Log.e("MemoryExtractor", "Error storing memory: $memory", e)
                            }
                        }
                    } else {
                        Log.d("MemoryExtractor", "No significant memories found in conversation")
                    }
                } else {
                    Log.e("MemoryExtractor", "Failed to get memory extraction response")
                }

            } catch (e: Exception) {
                Log.e("MemoryExtractor", "Error during memory extraction", e)
            }
        }
    }

    /**
     * Formats conversation history for memory extraction analysis
     */
    private fun formatConversationForExtraction(conversationHistory: List<Pair<String, List<String>>>): String {
        return conversationHistory.joinToString("\n") { (role, parts) ->
            // For Puter.js, we store messages as simple strings, so we can join them directly
            val text = parts.joinToString(" ")
            "$role: $text"
        }
    }
    
    /**
     * Parses the LLM response to extract individual memories
     */
    private fun parseExtractedMemories(response: String): List<String> {
        return try {
            response.lines()
                .filter { it.isNotBlank() }
                .filter { !it.equals("NO_MEMORIES", ignoreCase = true) }
                .filter { !it.startsWith("Extracted Memories") }
                .filter { !it.startsWith("Memories:") }
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        } catch (e: Exception) {
            Log.e("MemoryExtractor", "Error parsing extracted memories", e)
            emptyList()
        }
    }
    
    companion object {
        @Volatile
        private var INSTANCE: MemoryExtractor? = null
        
        fun getInstance(context: Context): MemoryExtractor {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MemoryExtractor(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}