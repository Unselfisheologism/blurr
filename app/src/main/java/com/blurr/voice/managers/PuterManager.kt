package com.blurr.voice.managers

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.blurr.voice.services.PuterService
import java.util.concurrent.CompletableFuture

class PuterManager(private val context: Context) {
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
        }
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
}

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
            puterService = null
            Log.d(TAG, "Disconnected from PuterService")
        }
}
    }
}

    

    fun chat(query: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterChat(query) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun txt2img(prompt: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterTxt2Img(prompt) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun img2txt(imageData: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterImg2Txt(imageData) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun txt2speech(text: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterTxt2Speech(text) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun kvGet(key: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.puterKvGet(key) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun kvSet(key: String, value: String): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(true)
        }
}

        puterService?.puterKvSet(key, value) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun kvDel(key: String): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response != null)
        }
}

        puterService?.puterKvDel(key) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun kvList(pattern: String = "*", returnValues: Boolean = false): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "[]")
        }
}

        puterService?.puterKvList(pattern, returnValues) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun kvIncr(key: String, amount: Int = 1): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.puterKvIncr(key, amount) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun kvDecr(key: String, amount: Int = 1): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.puterKvDecr(key, amount) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun kvFlush(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(true)
        }
}

        puterService?.puterKvFlush { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun signIn(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        puterService?.puterAuthSignIn { success ->
            future.complete(success)
        }
}

        return future
    }
}

    fun isSignedIn(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        puterService?.puterAuthIsSignedIn { signedIn ->
            future.complete(signedIn)
        }
}

        return future
    }
}

    fun getUser(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.puterAuthGetUser { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun getUserName(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        getUser().thenApply { userJson ->
            try {
                val user = org.json.JSONObject(userJson)
                val name = user.optString("username", "Unknown")
                future.complete(name)
            }
} catch (e: Exception) {
                future.complete("Unknown")
            }
}
        }
}.exceptionally { throwable ->
            future.complete("Unknown")
            null
        }
}
        return future
    }
}

    fun getUserEmail(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        getUser().thenApply { userJson ->
            try {
                val user = org.json.JSONObject(userJson)
                val email = user.optString("email", "unknown@example.com")
                future.complete(email)
            }
} catch (e: Exception) {
                future.complete("unknown@example.com")
            }
}
        }
}.exceptionally { throwable ->
            future.complete("unknown@example.com")
            null
        }
}
        return future
    }
}

    private fun getNextCallbackId(): String {
        return "callback_${callbackCounter++}
}"
    }
}

    fun initialize() {
        val intent = Intent(context, PuterService::class.java)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        context.startService(intent)
    }
}

    fun shutdown() {
        if (isBound) {
            context.unbindService(connection)
            isBound = false
        }
}
    }
}

    
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
                }
} else if (type == "complete") {
                    val data = jsonObject.optString("data")
                    future.complete(data)
                }
}
            }
} catch (e: Exception) {
                future.complete(response ?: "")
            }
}
        }
}

        puterService?.executePuterChatStream(query, onChunkCallback) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun fsWrite(path: String, data: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterFsWrite(path, data, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun fsRead(path: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterFsRead(path, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun fsMkdir(path: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterFsMkdir(path, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun fsReaddir(path: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "[]")
        }
}

        puterService?.executePuterFsReaddir(path) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun fsDelete(path: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterFsDelete(path, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun fsMove(source: String, destination: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterFsMove(source, destination, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun fsCopy(source: String, destination: String, options: Map<String, Any> = mapOf()): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        val optionsJson = org.json.JSONObject(options).toString()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterFsCopy(source, destination, optionsJson) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun fsRename(path: String, newName: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterFsRename(path, newName) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun fsStat(path: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterFsStat(path) { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    fun fsSpace(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()

        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
}

        puterService?.executePuterFsSpace { response ->
            callbacks.remove(callbackId)?.invoke(response)
        }
}

        return future
    }
}

    // Add missing methods that are being called in ConversationalAgentService
    fun trackEvent(eventName: String, properties: Map<String, String> = emptyMap()) {
        // This is a placeholder implementation
        Log.d(TAG, "Tracking event: $eventName with properties: $properties")
    }
}

    fun isUserSignedIn(): Boolean {
        // This is a placeholder implementation
        return true
    }
}

    fun getUserId(): String {
        // This is a placeholder implementation
        return "user123"
    }
}

    fun saveConversationToKvStore(key: String, data: Map<String, Any?>) {
        // This is a placeholder implementation
        Log.d(TAG, "Saving conversation to KV store with key: $key")
    }
}

    fun saveMessageToKvStore(key: String, data: Map<String, Any?>) {
        // This is a placeholder implementation
        Log.d(TAG, "Saving message to KV store with key: $key")
    }
}

    fun chatWithAI(prompt: String, modelName: String = "gpt-5-nano"): String? {
        // This is a placeholder implementation
        return "This is a placeholder response from the AI"
    }
}

    fun saveToKvStore(key: String, data: Map<String, Any?>) {
        // This is a placeholder implementation
        Log.d(TAG, "Saving data to KV store with key: $key")
    }
}

    fun getDeveloperMessage(): String? {
        // This is a placeholder implementation
        return "This is a developer message from Puter.js"
    }
}
}
}