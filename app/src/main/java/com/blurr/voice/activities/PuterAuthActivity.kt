package com.blurr.voice.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log

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
                       uri.getQueryParameter("access_token") ?: uri.getQueryParameter("auth_code")
            
            if (token != null) {
                Log.d(TAG, "Authentication successful, token received: $token")
                // Send the token back to the PuterService via broadcast
                sendTokenToService(token)
            } else {
                Log.e(TAG, "Authentication failed - no token in redirect URI")
                Log.e(TAG, "Full URI: $uri")
                // Send error back to the PuterService via broadcast
                sendErrorToService("No token in redirect URI: $uri")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling auth redirect", e)
            // Send error back to the PuterService via broadcast
            sendErrorToService("Error handling auth redirect: ${e.message}")
        } finally {
            // Close this activity as the auth flow is complete
            finish()
        }
    }
    
    private fun sendTokenToService(token: String) {
        // Create an intent to broadcast the token to the PuterService
        val intent = Intent("AUTH_SUCCESS").apply {
            putExtra("token", token)
        }
        
        // Send the broadcast
        sendBroadcast(intent)
        
        // Also set result for the activity result API
        setResult(RESULT_OK, Intent().apply {
            putExtra("token", token)
        })
    }
    
    private fun sendErrorToService(error: String) {
        // Create an intent to broadcast the error to the PuterService
        val intent = Intent("AUTH_ERROR").apply {
            putExtra("error", error)
        }
        
        // Send the broadcast
        sendBroadcast(intent)
        
        // Also set result for the activity result API
        setResult(RESULT_CANCELED, Intent().apply {
            putExtra("error", error)
        })
    }
}