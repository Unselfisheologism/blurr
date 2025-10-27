package com.blurr.voice.utilities

import android.graphics.Bitmap
import com.blurr.voice.api.LLMApi

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
    chat: List<Pair<String, List<String>>>
): String? {
    return LLMApi.generateContent(chat)
}
