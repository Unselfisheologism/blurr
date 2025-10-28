package com.blurr.voice.agents

import android.content.Context
import com.blurr.voice.managers.PuterManager  // Changed from LLMApi to PuterManager for puter.js
// REMOVED: import com.google.ai.client.generativeai.type.TextPart  // Removed Google Generative AI dependency
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import org.json.JSONException
import kotlinx.coroutines.future.await

/**
 * A data class to hold the parsed result from the clarification check for type safety.
 *
 * @property status The status of the instruction, either "CLEAR" or "NEEDS_CLARIFICATION".
 * @property questions A list of clarifying questions if the status is "NEEDS_CLARIFICATION".
 */
data class ClarificationResult(
    val status: String,
    val questions: List<String>
)

/**
 * An agent responsible for analyzing a user's task instruction to determine if it's
 * clear enough for execution or if it requires more information.
 * It communicates with the puter.js API using a structured JSON format for both requests and responses.
 */
class ClarificationAgent {

    /**
     * Analyzes the user's instruction and returns a result indicating if clarification is needed.
     * This is the main entry point for the agent.
     *
     * @param instruction The user's raw task instruction (e.g., "send a message to mom").
     * @param conversationHistory The recent history of the conversation for context.
     * @param context The Android context, required for the puter.js API call.
     * @return A [ClarificationResult] containing the status and any necessary questions.
     */
    suspend fun analyze(instruction: String, conversationHistory: List<Pair<String, List<Any>>>, context: Context): ClarificationResult {
        try {
            // 1. Create a specialized prompt for the LLM.
            val prompt = createPrompt(instruction, conversationHistory)

            // 2. Prepare the chat structure for the puter.js API.
            // For this specific task, we only need to send our structured prompt.
            val apiChat = listOf("user" to listOf(prompt))  // Changed from TextPart(prompt) to just prompt string

            // 3. Call the puter.js API using PuterManager.
            val puterManager = PuterManager.getInstance(context)
            val responseJsonFuture = withContext(Dispatchers.IO) {
                puterManager.chat(prompt) // Changed from LLMApi.generateContent to puterManager method
            }
            val responseJson = responseJsonFuture.await()
            Log.d("ClarificationAgent", "Clarification API Response: $responseJson")

            // 4. Parse the JSON response into our data class.
            return parseResponse(responseJson)

        } catch (e: Exception) {
            Log.e("ClarificationAgent", "Error during clarification analysis", e)
            // Fallback safety net: If any error occurs, assume the instruction is clear
            // to prevent the agent from getting stuck.
            return ClarificationResult("CLEAR", emptyList())
        }
    }

    /**
     * Parses the JSON response string from the puter.js API into a [ClarificationResult].
     * It's designed to be robust against common formatting issues like markdown code blocks.
     *
     * @param jsonResponse The raw JSON string from the API.
     * @return A [ClarificationResult] object. Returns a default "CLEAR" result on parsing failure.
     */
    private fun parseResponse(jsonResponse: String?): ClarificationResult {
        if (jsonResponse.isNullOrBlank()) {
            Log.w("ClarificationAgent", "Received null or blank response from API. Defaulting to CLEAR.")
            return ClarificationResult("CLEAR", emptyList())
        }
        try {
            // Clean the response to handle cases where the model wraps JSON in markdown.
            val cleanedJson = jsonResponse.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()

            val json = JSONObject(cleanedJson)
            val status = json.optString("status", "CLEAR")
            val questionsArray = json.optJSONArray("questions")
            val questions = mutableListOf<String>()

            if (questionsArray != null) {
                for (i in 0 until questionsArray.length()) {
                    questions.add(questionsArray.getString(i))
                }
            }
            return ClarificationResult(status, questions)
        } catch (e: JSONException) {
            Log.e("ClarificationAgent", "Failed to parse clarification JSON: $jsonResponse", e)
            // Fallback on parsing failure.
            return ClarificationResult("CLEAR", emptyList())
        }
    }

    /**
     * Creates the prompt for the puter.js API, instructing it to analyze the user's
     * instruction and respond with a specific JSON format.
     *
     * @param instruction The user's task instruction to analyze.
     * @param conversationHistory The recent conversation history for context.
     * @return A formatted prompt string.
     */
    private fun createPrompt(instruction: String, conversationHistory: List<Pair<String, List<Any>>>): String {
        // Build a concise version of the conversation history for context.
        val historyString = conversationHistory
            .takeLast(6) // Use last 6 turns to keep the prompt focused.
            .joinToString("\n") { (role, parts) ->
                // Changed from filterIsInstance<TextPart> to just handle strings directly
                val text = parts.joinToString(" ") { it.toString() }
                // Clean up the model's JSON response from the main conversation history.
                val cleanedText = if (role == "model" && text.trim().startsWith("{")) "[Agent performs an action]" else text
                "$role: $cleanedText"
            }

        return """
            You are an AI assistant that analyzes a user's instruction to determine if it requires clarification before an automated agent can execute it.
            Your goal is to identify ambiguous or incomplete instructions and generate specific, actionable questions to gather the missing details. The agent can see the screen, tap, and use the phone like a human.

            Analyze the user's latest instruction within the context of the recent conversation.

            ### Recent Conversation History ###
            $historyString
            ### End Conversation History ###

            ### User's Task Instruction to Analyze ###
            "$instruction"

            ### Your Task ###
            Based on the instruction and the conversation history, decide if the instruction is clear enough to be executed or if it needs clarification.
            - An instruction is CLEAR if it can be performed without any more information (e.g., "Open WhatsApp", "Take a screenshot").
            - An instruction NEEDS CLARIFICATION if it's missing key details (e.g., "Send a message" (to whom? what message?), "Set an alarm" (for what time?), "Book a ride" (to where?)).

            ### Response Format ###
            You MUST respond with a single, valid JSON object only. Do not add any text before or after the JSON.
            The JSON object must have the following structure:
            {
              "status": "CLEAR" | "NEEDS_CLARIFICATION",
              "questions": [ "An array of strings, where each string is a specific clarifying question." ]
            }

            Example 1 (Needs Clarification):
            Instruction: "Message my brother happy birthday"
            Response:
            {
              "status": "NEEDS_CLARIFICATION",
              "questions": [
                "Which of your brothers should I message?",
                "Which app should I use to send the message?"
              ]
            }

            Example 2 (Clear):
            Instruction: "Go to the home screen"
            Response:
            {
              "status": "CLEAR",
              "questions": []
            }
        """.trimIndent()
    }
}