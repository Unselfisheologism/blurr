package com.blurr.voice.utilities

import android.content.Context
import android.graphics.Bitmap
import com.blurr.voice.managers.PuterManager
import kotlinx.coroutines.future.await

/**
 * Adds a user or model response to the chat history.
 * 
 * @param role The role of the speaker ("user" or "model")
 * @param prompt The text content of the message
 * @param chatHistory The current chat history
 * @return Updated chat history with the new message added
 */
fun addResponse(
    role: String,
    prompt: String,
    chatHistory: List<Pair<String, List<String>>>
): List<Pair<String, List<String>>> {
    val updatedChat = chatHistory.toMutableList()
    
    // For Puter.js, we store messages as simple strings
    val messageParts = listOf(prompt)
    
    updatedChat.add(Pair(role, messageParts))
    return updatedChat
}

/**
 * Adds a response with pre and post images (kept for compatibility but not used with Puter.js)
 */
fun addResponsePrePost(
    role: String,
    prompt: String,
    chatHistory: List<Pair<String, List<String>>>,
    imageBefore: Bitmap? = null,
    imageAfter: Bitmap? = null
): List<Pair<String, List<String>>> {
    // For Puter.js, we just add the text prompt
    return addResponse(role, prompt, chatHistory)
}

/**
 * Gets a response from the reasoning model API using Puter.js
 */
suspend fun getReasoningModelApiResponse(
    chat: List<Pair<String, List<String>>>,
    context: Context // Add context parameter
): String? {
    // Get the last message from the chat to send to puter.js
    val lastMessage = chat.lastOrNull()?.second?.lastOrNull() ?: ""
    val historyContext = chat.dropLast(1).map { (role, parts) -> "$role: ${parts.joinToString(" ")}" }.joinToString("\n")
    val fullPrompt = if (historyContext.isNotEmpty()) "$historyContext\nuser: $lastMessage" else lastMessage
    
    val puterManager = PuterManager.getInstance(context)
    val future = puterManager.chat(fullPrompt) // Changed from LLMApi.generateContent to puterManager method
    return future.await()
}
