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

    companion object {
        const val TAG = "PuterManager"
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

    fun signIn(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        puterService?.puterAuthSignIn { success ->
            future.complete(success)
        }

        return future
    }

    fun isSignedIn(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        puterService?.puterAuthIsSignedIn { signedIn ->
            future.complete(signedIn)
        }

        return future
    }

    private fun getNextCallbackId(): String {
        return "callback_${callbackCounter++}"
    }

    // Singleton instance
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