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
            // Extract token or auth data from the redirect URI
            val token = uri.getQueryParameter("token") ?: uri.getQueryParameter("auth_token")
            
            if (token != null) {
                Log.d(TAG, "Authentication successful, token received")
                // The PuterService should already be handling this via its WebViewClient
                // We just need to finish this activity
            } else {
                Log.e(TAG, "Authentication failed - no token in redirect URI")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling auth redirect", e)
        } finally {
            // Close this activity as the auth flow is complete
            finish()
        }
    }
}