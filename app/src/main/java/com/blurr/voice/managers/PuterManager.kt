package com.blurr.voice.managers

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.blurr.voice.R
import com.blurr.voice.services.PuterService
import com.blurr.voice.data.TaskHistoryItem
import java.util.concurrent.CompletableFuture

class PuterManager private constructor(private val context: Context) {
    private var puterService: PuterService? = null
    private var isBound = false
    private val callbacks = mutableMapOf<String, (String?) -> Unit>()
    private var callbackCounter = 0
    private val TAG = "PuterManager"

    companion object {
        @Volatile
        private var INSTANCE: PuterManager? = null

        fun getInstance(context: Context): PuterManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PuterManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as PuterService.PuterBinder
            puterService = binder.getService()
            isBound = true
            Log.d(TAG, "Connected to PuterService")
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
            puterService = null
            Log.d(TAG, "Disconnected from PuterService")
        }
    }

    fun initialize() {
        val intent = Intent(context, PuterService::class.java)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        context.startService(intent)
    }

    fun shutdown() {
        if (isBound) {
            context.unbindService(connection)
            isBound = false
        }
    }
    
    val isServiceBound: Boolean
        get() = this.isBound
    
    fun getPuterService(): PuterService? {
        return puterService
    }

    private fun getNextCallbackId(): String {
        return "callback_${callbackCounter++}"
    }

    // Authentication sign in functionality - now works with popup WebView
    fun signIn(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        
        // Set the callback in the service to be called when authentication completes
        puterService?.signInCallback = { success ->
            future.complete(success)
            puterService?.signInCallback = null
        }

        // First check if already signed in
        puterService?.puterAuthIsSignedIn { signedIn: Boolean ->
            if (signedIn) {
                // Already signed in
                future.complete(true)
                puterService?.signInCallback = null
            } else {
                // Not signed in, initiate sign in process through popup WebView
                puterService?.evaluateJavascript("""
                    (function() {
                        try {
                            console.log("Attempting to sign in with Puter.js via popup");
                            puter.auth.signIn().then(user => {
                                console.log("Authentication successful", user);
                                if (window.AndroidInterface) {
                                    window.AndroidInterface.onAuthSuccess(JSON.stringify(user));
                                }
                            }).catch(error => {
                                console.error("Authentication error", error);
                                if (window.AndroidInterface) {
                                    window.AndroidInterface.onAuthError(error.message);
                                }
                            });
                        } catch (e) {
                            console.error("Error calling signIn", e);
                            if (window.AndroidInterface) {
                                window.AndroidInterface.onAuthError("Failed to initialize sign-in: " + e.message);
                            }
                        }
                    })();
                """.trimIndent(), null)
            }
        }
        
        return future
    }
    
    // Initialize Puter with an authentication token
    fun initializeWithToken(token: String) {  
        // Store the token for future use  
        val editor = context.getSharedPreferences("auth", Context.MODE_PRIVATE).edit()  
        editor.putString("puter_token", token)  
        editor.apply()  
      
        // Inject token into the WebView's JavaScript context  
        if (isBound && puterService != null) {  
            puterService?.injectAuthToken(token)  
            Log.d(TAG, "Token injected into PuterService WebView")  
        } else {  
            Log.w(TAG, "PuterService not bound, token stored but not injected")  
        }  
    }
    
    // Get the stored authentication token
    fun getAuthToken(): String? {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("puter_token", null)
    }
    
    // Check if user is authenticated by checking for stored token
    fun isAuthenticated(): Boolean {
        return getAuthToken() != null
    }
    
    // Sign out functionality
    fun signOut() {
        val editor = context.getSharedPreferences("auth", Context.MODE_PRIVATE).edit()
        editor.remove("puter_token")
        editor.apply()
        
        if (isBound) {
            // We could potentially notify the service to clear the session
            // puterService?.clearAuthToken()
        }
    }

    // Chat functionality
    fun chat(query: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterChat(query) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // Text to image functionality
    fun txt2img(prompt: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterTxt2Img(prompt) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // Image to text functionality
    fun img2txt(imageData: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterImg2Txt(imageData) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // Text to speech functionality
    fun txt2speech(text: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterTxt2Speech(text) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // KV store get functionality
    fun kvGet(key: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.puterKvGet(key) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // KV store set functionality
    fun kvSet(key: String, value: String): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(true)
        }

        puterService?.puterKvSet(key, value) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // KV store delete functionality
    fun kvDel(key: String): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response != null)
        }

        puterService?.puterKvDel(key) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // KV store list functionality
    fun kvList(pattern: String = "*", returnValues: Boolean = false): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "[]")
        }

        puterService?.puterKvList(pattern, returnValues) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // KV store increment functionality
    fun kvIncr(key: String, amount: Int = 1): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.puterKvIncr(key, amount) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // KV store decrement functionality
    fun kvDecr(key: String, amount: Int = 1): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.puterKvDecr(key, amount) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // KV store flush functionality
    fun kvFlush(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(true)
        }

        puterService?.puterKvFlush { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // Authentication check if signed in functionality
    fun isSignedIn(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        puterService?.puterAuthIsSignedIn { signedIn: Boolean ->
            future.complete(signedIn)
        }

        return future
    }

    // Authentication get user functionality
    fun getUser(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.puterAuthGetUser { response: String? ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // Authentication get user name functionality
    fun getUserName(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        getUser().thenApply { userJson ->
            try {
                val user = org.json.JSONObject(userJson)
                val name = user.optString("username", "Unknown")
                future.complete(name)
            } catch (e: Exception) {
                future.complete("Unknown")
            }
        }.exceptionally { throwable ->
            future.complete("Unknown")
            null
        }
        return future
    }

    // Authentication get user email functionality
    fun getUserEmail(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        getUser().thenApply { userJson ->
            try {
                val user = org.json.JSONObject(userJson)
                val email = user.optString("email", "unknown@example.com")
                future.complete(email)
            } catch (e: Exception) {
                future.complete("unknown@example.com")
            }
        }.exceptionally { throwable ->
            future.complete("unknown@example.com")
            null
        }
        return future
    }

    // Chat streaming functionality
    fun chatStream(query: String, onChunkCallback: (String) -> Unit): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            try {
                val jsonObject = org.json.JSONObject(response ?: "")
                val type = jsonObject.optString("type")
                if (type == "chunk") {
                    val data = jsonObject.optString("data")
                    onChunkCallback(data)
                } else if (type == "complete") {
                    val data = jsonObject.optString("data")
                    future.complete(data)
                }
            } catch (e: Exception) {
                future.complete(response ?: "")
            }
        }

        puterService?.executePuterChatStream(query, onChunkCallback) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // File system write functionality
    fun fsWrite(path: String, data: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterFsWrite(path, data, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // File system read functionality
    fun fsRead(path: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterFsRead(path, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // File system mkdir functionality
    fun fsMkdir(path: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterFsMkdir(path, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // File system readdir functionality
    fun fsReaddir(path: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "[]")
        }

        puterService?.executePuterFsReaddir(path) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // File system delete functionality
    fun fsDelete(path: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterFsDelete(path, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // File system move functionality
    fun fsMove(source: String, destination: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterFsMove(source, destination, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // File system copy functionality
    fun fsCopy(source: String, destination: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterFsCopy(source, destination, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // File system rename functionality
    fun fsRename(path: String, newName: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterFsRename(path, newName) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // File system stat functionality
    fun fsStat(path: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterFsStat(path) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // File system space functionality
    fun fsSpace(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }

        puterService?.executePuterFsSpace { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }
    
    fun getTaskHistoryFromKvStore(): CompletableFuture<List<TaskHistoryItem>> {
        val future = CompletableFuture<List<TaskHistoryItem>>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            try {
                val jsonArray = org.json.JSONArray(response ?: "[]")
                val taskHistory = mutableListOf<TaskHistoryItem>()
                
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val key = item.optString("key")
                    if (key.startsWith("task_")) {  // Only process task items
                        val valueStr = item.optString("value", "{}")
                        val value = org.json.JSONObject(valueStr)
                        
                        val taskHistoryItem = TaskHistoryItem(
                            task = value.optString("task", ""),
                            status = value.optString("status", ""),
                            startedAt = if (value.has("startedAt")) value.optLong("startedAt") else null,
                            completedAt = if (value.has("completedAt")) value.optLong("completedAt") else null,
                            success = if (value.has("success")) value.optBoolean("success") else null,
                            errorMessage = if (value.has("errorMessage")) value.optString("errorMessage", "") else null
                        )
                        taskHistory.add(taskHistoryItem)
                    }
                }
                
                future.complete(taskHistory)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing task history", e)
                future.complete(emptyList())
            }
        }

        puterService?.puterGetTaskHistoryFromKvStore { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }
    
    fun saveTaskToKvStore(key: String, taskDataJson: String): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val callbackId = getNextCallbackId()
        
        // Convert the task data to JSON string
        val taskDataJsonStr = try {
            org.json.JSONObject(taskDataJson).toString()
        } catch (e: Exception) {
            Log.e(TAG, "Error converting task data to JSON", e)
            "{}"
        }

        callbacks[callbackId] = { response ->
            future.complete(true)
        }

        puterService?.puterSaveTaskToKvStore(key, taskDataJsonStr) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }

        return future
    }

    // Additional helper methods
    fun trackEvent(eventName: String, properties: Map<String, String> = emptyMap()) {
        // This is a placeholder implementation
        Log.d(TAG, "Tracking event: $eventName with properties: $properties")
    }

    fun isUserSignedIn(): Boolean {
        var result = false
        if (isBound) {
            val latch = java.util.concurrent.CountDownLatch(1)
            puterService?.puterAuthIsSignedIn { signedIn: Boolean ->
                result = signedIn
                latch.countDown()
            }
            try {
                // Wait for a maximum of 5 seconds for the authentication check
                latch.await(5, java.util.concurrent.TimeUnit.SECONDS)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
        return result
    }

    fun getUserId(): String {
        // This is a placeholder implementation
        return "user123"
    }

    fun saveConversationToKvStore(key: String, data: Map<String, Any?>) {
        // This is a placeholder implementation
        Log.d(TAG, "Saving conversation to KV store with key: $key")
    }

    fun saveMessageToKvStore(key: String, data: Map<String, Any?>) {
        // This is a placeholder implementation
        Log.d(TAG, "Saving message to KV store with key: $key")
    }

    fun chatWithAI(prompt: String, modelName: String = "gpt-5-nano"): String? {
        // This is a placeholder implementation
        return "This is a placeholder response from the AI"
    }

    fun saveToKvStore(key: String, data: Map<String, Any?>) {
        // This is a placeholder implementation
        Log.d(TAG, "Saving data to KV store with key: $key")
    }

    fun getDeveloperMessage(): String? {
        // This is a placeholder implementation
        return "This is a developer message from Puter.js"
    }

    fun synthesizeTextToSpeech(text: String): ByteArray {
        // This is a placeholder implementation
        return byteArrayOf()
    }
}