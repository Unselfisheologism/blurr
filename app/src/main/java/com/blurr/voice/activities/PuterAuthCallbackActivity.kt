package com.blurr.voice.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.blurr.voice.managers.PuterManager
import com.blurr.voice.services.PuterService

class PuterAuthCallbackActivity : Activity() {
    private val TAG = "PuterAuthCallbackActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "PuterAuthCallbackActivity created")

        // Handle the authentication callback
        handleAuthCallback(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "PuterAuthCallbackActivity received new intent")
        handleAuthCallback(intent)
    }

    private fun handleAuthCallback(intent: Intent?) {
        val uri: Uri? = intent?.data
        Log.d(TAG, "Received URI: $uri")

        if (uri != null) {
            // Extract the token from the URI
            val token = uri.getQueryParameter("token")
            Log.d(TAG, "Extracted token: $token")

            if (!token.isNullOrEmpty()) {
                // Store the token in PuterManager
                val puterManager = PuterManager.getInstance(this)
                puterManager.initializeWithToken(token)

                // Send a broadcast to notify the service about the successful authentication
                val authIntent = Intent("com.blurr.voice.PUTER_AUTH_SUCCESS")
                authIntent.putExtra("token", token)
                sendBroadcast(authIntent)
            } else {
                Log.e(TAG, "No token found in the URI")
                // Send a broadcast to notify about the failed authentication
                val authIntent = Intent("com.blurr.voice.PUTER_AUTH_FAILED")
                sendBroadcast(authIntent)
            }
        }

        // Close this activity as it's done its job
        finish()
    }
}