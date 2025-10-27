package com.blurr.voice.api

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.blurr.voice.MyApplication
import com.blurr.voice.managers.PuterManager
import com.blurr.voice.utilities.NetworkConnectivityManager
import com.blurr.voice.utilities.NetworkNotifier
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Refactored LLM API as a singleton object using Puter.js.
 * It now uses Puter.js for LLM functionality instead of Google Gemini
 * and logs all requests and responses to a persistent file.
 */
object LLMApi {
    

    suspend fun generateContent(
        chat: List<Pair<String, List<Any>>>,
        images: List<Bitmap> = emptyList(), // Kept for compatibility but not used with Puter.js
        modelName: String = "gpt-5-nano", // Using Puter.js compatible model
        maxRetry: Int = 4, // Kept for compatibility but not used with Puter.js
        context: Context? = null
    ): String? {
        // Network check before making any calls
        try {
            val appCtx = context ?: MyApplication.appContext
            val isOnline = NetworkConnectivityManager(appCtx).isNetworkAvailable()
            if (!isOnline) {
                Log.e("LLMApi", "No internet connection. Skipping generateContent call.")
                NetworkNotifier.notifyOffline()
                return null
            }
        } catch (e: Exception) {
            Log.e("LLMApi", "Network check failed, assuming offline. ${e.message}")
            return null
        }

        // Extract the last user prompt text for logging purposes
        val lastUserPrompt = chat.lastOrNull { it.first == "user" }
            ?.second
            ?.joinToString(separator = "\n") { it.toString() } ?: "No text prompt found"

        Log.d("LLMApi", "=== PUTER.JS LLM REQUEST ===")
        Log.d("LLMApi", "Model: $modelName")
        Log.d("LLMApi", "Prompt: $lastUserPrompt")

        try {
            // Use Puter.js for LLM functionality
            val puterResponse = PuterManager.getInstance(context ?: MyApplication.appContext)
                .chatWithAI(lastUserPrompt, modelName)
            
            Log.d("LLMApi", "=== PUTER.JS LLM RESPONSE ===")
            Log.d("LLMApi", "Response: $puterResponse")
            
            // Log successful response
            val logEntry = createLogEntry(
                attempt = 1,
                modelName = modelName,
                prompt = lastUserPrompt,
                imagesCount = 0, // Not using images with Puter.js
                payload = lastUserPrompt, // Simplified for Puter.js
                responseCode = 200, // Simulated success code
                responseBody = puterResponse,
                responseTime = 0, // Timing not tracked with Puter.js
                totalTime = 0 // Timing not tracked with Puter.js
            )
            saveLogToFile(MyApplication.appContext, logEntry)
            val logData = createLogDataMap(
                attempt = 1,
                modelName = modelName,
                prompt = lastUserPrompt,
                imagesCount = 0, // Not using images with Puter.js
                responseCode = 200, // Simulated success code
                responseTime = 0,
                totalTime = 0,
                responseBody = puterResponse,
                status = "pass",
            )
            logToPuterKvStore(logData)

            return puterResponse
        } catch (e: Exception) {
            Log.e("LLMApi", "=== PUTER.JS LLM ERROR ===", e)

            // Log error
            val logEntry = createLogEntry(
                attempt = 1,
                modelName = modelName,
                prompt = lastUserPrompt,
                imagesCount = 0, // Not using images with Puter.js
                payload = lastUserPrompt,
                responseCode = null,
                responseBody = null,
                responseTime = 0,
                totalTime = 0,
                error = e.message
            )
            saveLogToFile(MyApplication.appContext, logEntry)
            val logData = createLogDataMap(
                attempt = 1,
                modelName = modelName,
                prompt = lastUserPrompt,
                imagesCount = 0, // Not using images with Puter.js
                responseCode = null,
                responseTime = 0,
                totalTime = 0,
                status = "error",
                responseBody = null,
                error = e.message
            )
            logToPuterKvStore(logData)

            return null
        }
    }

    /**
     * Builds a simplified payload for Puter.js LLM requests
     * Puter.js uses a simpler format focusing on the main prompt text
     */
    private fun buildPayload(chat: List<Pair<String, List<Any>>>, modelName: String): JSONObject {
        val rootObject = JSONObject()
        rootObject.put("modelName", modelName)

        // For Puter.js, we'll just use the last user message as the prompt
        val lastUserMessage = chat.lastOrNull { it.first == "user" }
        if (lastUserMessage != null) {
            val combinedText = lastUserMessage.second.joinToString("\n") { it.toString() }
            rootObject.put("prompt", combinedText)
        }

        return rootObject
    }

    /**
     * Parses responses from Puter.js LLM API
     * Puter.js responses are typically simpler than Gemini API responses
     */
    private fun parseSuccessResponse(responseBody: String): String? {
        return try {
            // First try to parse as JSON
            val json = JSONObject(responseBody)
            
            // Handle Puter.js text response format
            if (json.has("text")) {
                return json.getString("text")
            }
            
            // Handle direct string responses from Puter.js
            if (json.has("response")) {
                return json.getString("response")
            }
            
            // If it's a simple JSON object with a string value, return the whole thing as string
            return responseBody
        } catch (e: Exception) {
            // If it's not valid JSON, treat as plain text response
            responseBody
        }
    }


    private fun saveLogToFile(context: Context, logEntry: String) {
        try {
            val logDir = File(context.filesDir, "llm_logs")
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            // Use a single, rolling log file for simplicity.
            val logFile = File(logDir, "llm_api_log.txt")

            FileWriter(logFile, true).use { writer ->
                writer.append(logEntry)
            }
            Log.d("LLMApi", "Log entry saved to: ${logFile.absolutePath}")

        } catch (e: Exception) {
            Log.e("LLMApi", "Failed to save log to file", e)
        }
    }
    
    private fun logToPuterKvStore(logData: Map<String, Any?>) {
        try {
            val puterManager = PuterManager.getInstance(MyApplication.appContext)
            
            // Create a unique and descriptive ID from the timestamp and prompt
            val timestamp = System.currentTimeMillis()
            val promptSnippet = (logData["prompt"] as? String)?.take(40) ?: "log"

            // Sanitize the prompt snippet to be a valid key
            // (removes spaces and special characters)
            val sanitizedPrompt = promptSnippet.replace(Regex("[^a-zA-Z0-9]"), "_")

            val key = "${timestamp}_$sanitizedPrompt"

            // Save to puter.js key-value store
            puterManager.saveToKvStore("llm_log_$key", logData)
            
            Log.d("LLMApi", "Log sent to puter.js key-value store with key: llm_log_$key")
        } catch (e: Exception) {
            // This listener is for debugging; it won't block your app's flow
            Log.e("LLMApi", "Error sending log to puter.js key-value store", e)
        }
    }
    
    private fun createLogEntry(
        attempt: Int,
        modelName: String,
        prompt: String,
        imagesCount: Int,
        payload: String,
        responseCode: Int?,
        responseBody: String?,
        responseTime: Long,
        totalTime: Long,
        error: String? = null
    ): String {
        return buildString {
            appendLine("=== PUTER.JS LLM DEBUG LOG ===")
            appendLine("Timestamp: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())}")
            appendLine("Attempt: $attempt")
            appendLine("Model: $modelName")
            appendLine("Images count: $imagesCount")
            appendLine("Prompt length: ${prompt.length}")
            appendLine("Prompt: $prompt")
            appendLine("Payload: $payload")
            appendLine("Response code: $responseCode")
            appendLine("Response time: ${responseTime}ms")
            appendLine("Total time: ${totalTime}ms")
            if (error != null) {
                appendLine("Error: $error")
            } else {
                appendLine("Response body: $responseBody")
            }
            appendLine("=== END LOG ===")
        }
    }
    
    private fun createLogDataMap(
        attempt: Int,
        modelName: String,
        prompt: String,
        imagesCount: Int,
        responseCode: Int?,
        responseTime: Long,
        totalTime: Long,
        status: String,
        responseBody: String?,
        error: String? = null
    ): Map<String, Any?> {
        return mapOf(
            "timestamp" to System.currentTimeMillis(), // Use server time
            "status" to status,
            "attempt" to attempt,
            "model" to modelName,
            "prompt" to prompt,
            "imagesCount" to imagesCount,
            "responseCode" to responseCode,
            "responseTimeMs" to responseTime,
            "totalTimeMs" to totalTime,
            "llmReply" to responseBody,
            "error" to error
        )
    }
}