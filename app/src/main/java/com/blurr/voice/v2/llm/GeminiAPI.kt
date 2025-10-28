package com.blurr.voice.v2.llm

import android.content.Context
import android.util.Log
import com.blurr.voice.BuildConfig
import com.blurr.voice.utilities.ApiKeyManager
import com.blurr.voice.v2.AgentOutput
import com.blurr.voice.managers.PuterManager
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

/**
 * A modern, robust AI API client using Puter.js.
 *
 * This client features:
 * - Conversion of internal message formats to the Puter.js format.
 * - An idiomatic, exponential backoff retry mechanism for API calls.
 *
 * @property context The application context.
 * @property modelName The name of the model to use.
 * @property maxRetry The maximum number of times to retry a failed API call.
 */
class GeminiApi(
    private val context: Context,
    private val modelName: String,
    private val maxRetry: Int = 3
) {

    companion object {
        private const val TAG = "GeminiV2Api"
        private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
    }

    /**
     * Generates a structured response from the AI model using Puter.js and parses it into an [AgentOutput] object.
     * This is the primary public method for this class.
     *
     * @param messages The list of [GeminiMessage] objects for the prompt.
     * @return An [AgentOutput] object on success, or null if the API call or parsing fails after all retries.
     */
    suspend fun generateAgentOutput(messages: List<GeminiMessage>): AgentOutput? {
        val jsonString = retryWithBackoff(times = maxRetry) {
            performApiCall(messages)
        } ?: return null

        return try {
            Log.d(TAG, "Parsing guaranteed JSON response. $jsonString")
            Log.d("GEMINIAPITEMP_OUTPUT", jsonString)
            Json.decodeFromString<AgentOutput>(jsonString)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse JSON into AgentOutput. Error: ${e.message}", e)
            null
        }
    }

    /**
     * Performs the API call using Puter.js
     */
    private suspend fun performApiCall(messages: List<GeminiMessage>): String {
        Log.i(TAG, "Using Puter.js for AI call.")
        return performPuterApiCall(messages)
    }

    /**
     * PUTER MODE: Performs the API call using Puter.js
     */
    private suspend fun performPuterApiCall(messages: List<GeminiMessage>): String {
        val puterManager = PuterManager.getInstance(context)
        
        // Convert messages to a simple string format for Puter
        val prompt = messages.map { message ->
            val role = when (message.role) {
                MessageRole.USER -> "User"
                MessageRole.MODEL -> "Assistant" 
                MessageRole.TOOL -> "Tool"
            }
            "${role}: ${message.parts.filterIsInstance<TextPart>().joinToString(" ") { it.text }}"
        }.joinToString("\n")
        
        // Call Puter.js and handle potential null return
        val response = puterManager.chatWithAI(prompt)
        return response ?: "{}" // Return empty JSON object if null
    }

    /**
     * WORKAROUND: Generates content using Puter.js for grounded content.
     *
     * @param prompt The user's text prompt.
     * @return The generated text content as a String, or null on failure.
     */
    suspend fun generateGroundedContent(prompt: String): String? {
        val puterManager = PuterManager.getInstance(context)
        
        return try {
            val response = puterManager.chatWithAI(prompt)
            response // Return the response directly, which may be null
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Puter API call", e)
            null
        }
    }

}

@Serializable
private data class ProxyRequestPart(val text: String)

@Serializable
private data class ProxyRequestMessage(val role: String, val parts: List<ProxyRequestPart>)

@Serializable
private data class ProxyRequestBody(val modelName: String, val messages: List<ProxyRequestMessage>)


/**
 * Custom exception to indicate that the response content was blocked by the API.
 */
class ContentBlockedException(message: String) : Exception(message)

/**
 * A higher-order function that provides a generic retry mechanism with exponential backoff.
 *
 * @param times The maximum number of retry attempts.
 * @param initialDelay The initial delay in milliseconds before the first retry.
 * @param maxDelay The maximum delay in milliseconds.
 * @param factor The multiplier for the delay on each subsequent retry.
 * @param block The suspend block of code to execute and retry on failure.
 * @return The result of the block if successful, or null if all retries fail.
 */
private suspend fun <T> retryWithBackoff(
    times: Int,
    initialDelay: Long = 1000L, // 1 second
    maxDelay: Long = 16000L,   // 16 seconds
    factor: Double = 2.0,
    block: suspend () -> T
): T? {
    var currentDelay = initialDelay
    repeat(times) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            Log.e("RetryUtil", "Attempt ${attempt + 1}/$times failed: ${e.message}", e)
            if (attempt == times - 1) {
                Log.e("RetryUtil", "All $times retry attempts failed.")
                return null // All retries failed
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
    }
    return null // Should not be reached
}