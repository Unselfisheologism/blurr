package com.blurr.voice.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.blurr.voice.R
import com.blurr.voice.managers.PuterManager
import java.util.concurrent.CompletableFuture

class PuterTestActivity : Activity() {
    private lateinit var puterManager: PuterManager
    private lateinit var resultTextView: TextView
    private lateinit var chatButton: Button
    private lateinit var signInButton: Button
    private lateinit var kvGetButton: Button
    private lateinit var kvSetButton: Button

    companion object {
        const val TAG = "PuterTestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puter_demo)

        // Initialize PuterManager
        puterManager = PuterManager.getInstance(this)

        // Initialize views
        resultTextView = findViewById(R.id.result_text)
        chatButton = findViewById(R.id.chat_button)
        signInButton = findViewById(R.id.sign_in_button)
        kvGetButton = findViewById(R.id.kv_get_button)
        kvSetButton = findViewById(R.id.kv_set_button)

        // Set click listeners
        chatButton.setOnClickListener {
            executeChat()
        }

        signInButton.setOnClickListener {
            signIn()
        }

        kvGetButton.setOnClickListener {
            kvGet()
        }

        kvSetButton.setOnClickListener {
            kvSet()
        }

        // Initialize PuterManager
        puterManager.initialize()
    }

    private fun executeChat() {
        resultTextView.text = "Executing chat..."
        
        // Execute a simple chat query
        val future: CompletableFuture<String> = puterManager.chat("Hello, how are you?")
        
        future.whenComplete { response, throwable ->
            runOnUiThread {
                if (throwable != null) {
                    resultTextView.text = "Error: ${throwable.message}"
                    Log.e(TAG, "Chat error", throwable)
                } else {
                    resultTextView.text = "Response: $response"
                    Log.d(TAG, "Chat response: $response")
                }
            }
        }
    }

    private fun signIn() {
        resultTextView.text = "Signing in..."
        
        // Attempt to sign in
        val future: CompletableFuture<Boolean> = puterManager.signIn()
        
        future.whenComplete { success, throwable ->
            runOnUiThread {
                if (throwable != null) {
                    resultTextView.text = "Sign in error: ${throwable.message}"
                    Log.e(TAG, "Sign in error", throwable)
                    Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                } else {
                    if (success) {
                        resultTextView.text = "Signed in successfully!"
                        Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        resultTextView.text = "Sign in failed"
                        Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun kvGet() {
        resultTextView.text = "Getting KV value..."
        
        // Get a value from KV store
        val future: CompletableFuture<String> = puterManager.kvGet("demo_key")
        
        future.whenComplete { response, throwable ->
            runOnUiThread {
                if (throwable != null) {
                    resultTextView.text = "KV Get error: ${throwable.message}"
                    Log.e(TAG, "KV Get error", throwable)
                } else {
                    resultTextView.text = "KV Value: $response"
                    Log.d(TAG, "KV Get response: $response")
                }
            }
        }
    }

    private fun kvSet() {
        resultTextView.text = "Setting KV value..."
        
        // Set a value in KV store
        val future: CompletableFuture<Boolean> = puterManager.kvSet("demo_key", "demo_value")
        
        future.whenComplete { success, throwable ->
            runOnUiThread {
                if (throwable != null) {
                    resultTextView.text = "KV Set error: ${throwable.message}"
                    Log.e(TAG, "KV Set error", throwable)
                } else {
                    if (success) {
                        resultTextView.text = "KV Set successful!"
                        Log.d(TAG, "KV Set successful")
                    } else {
                        resultTextView.text = "KV Set failed"
                        Log.e(TAG, "KV Set failed")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Shutdown PuterManager
        puterManager.shutdown()
    }
}