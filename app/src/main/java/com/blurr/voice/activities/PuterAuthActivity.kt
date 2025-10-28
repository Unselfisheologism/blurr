package com.blurr.voice.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.blurr.voice.services.PuterService

class PuterAuthActivity : Activity() {
    companion object {
        const val TAG = "PuterAuthActivity"
        const val AUTH_REDIRECT_URI = "blurr://puter-auth-callback"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // This activity is launched when the auth flow redirects back to our deep link
        val data = intent.data
        if (data != null && data.toString().startsWith(AUTH_REDIRECT_URI)) {
            // Process the authentication result and send it back to the service
            handleAuthRedirect(data)
        } else {
            // If this isn't a redirect, just finish
            finish()
        }
    }

    private fun handleAuthRedirect(uri: Uri) {
        try {
            Log.d(TAG, "Handling auth redirect: $uri")
            
            // Extract token or auth data from the redirect URI
            val token = uri.getQueryParameter("token") ?: uri.getQueryParameter("auth_token") ?: 
                       uri.getQueryParameter("access_token")
            
            if (token != null) {
                Log.d(TAG, "Authentication successful, token received")
                // Send the token back to the PuterService
                sendTokenToService(token)
            } else {
                Log.e(TAG, "Authentication failed - no token in redirect URI")
                // Send error back to the PuterService
                sendErrorToService("No token in redirect URI")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling auth redirect", e)
            // Send error back to the PuterService
            sendErrorToService("Error handling auth redirect: ${e.message}")
        } finally {
            // Close this activity as the auth flow is complete
            finish()
        }
    }
    
    private fun sendTokenToService(token: String) {
        // Create an intent to send the token back to the PuterService
        val intent = Intent(this, PuterService::class.java).apply {
            action = "AUTH_SUCCESS"
            putExtra("token", token)
        }
        
        // Send the broadcast to the service
        sendBroadcast(intent)
        
        // Also set result for the activity result API
        setResult(RESULT_OK, Intent().apply {
            putExtra("token", token)
        })
    }
    
    private fun sendErrorToService(error: String) {
        // Create an intent to send the error back to the PuterService
        val intent = Intent(this, PuterService::class.java).apply {
            action = "AUTH_ERROR"
            putExtra("error", error)
        }
        
        // Send the broadcast to the service
        sendBroadcast(intent)
        
        // Also set result for the activity result API
        setResult(RESULT_CANCELED, Intent().apply {
            putExtra("error", error)
        })
    }
}