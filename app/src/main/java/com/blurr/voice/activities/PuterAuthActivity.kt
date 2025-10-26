package com.blurr.voice.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import com.blurr.voice.R

class PuterAuthActivity : Activity() {
    companion object {
        const val TAG = "PuterAuthActivity"
        const val AUTH_REDIRECT_URI = "blurr://puter-auth-callback"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get the auth URL from intent
        val authUrl = intent.getStringExtra("auth_url")
        
        if (authUrl != null) {
            openAuthInCustomTab(authUrl)
        } else {
            finish()
        }
    }

    private fun openAuthInCustomTab(authUrl: String) {
        try {
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
            
            // Add redirect URI as a parameter to the auth URL
            val uriBuilder = Uri.parse(authUrl).buildUpon()
            uriBuilder.appendQueryParameter("redirect_uri", AUTH_REDIRECT_URI)
            
            customTabsIntent.launchUrl(this, uriBuilder.build())
        } catch (e: Exception) {
            Log.e(TAG, "Error opening custom tab", e)
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        
        // Handle redirect back from authentication
        val data = intent?.data
        if (data != null && data.toString().startsWith(AUTH_REDIRECT_URI)) {
            handleAuthRedirect(data)
        }
    }

    private fun handleAuthRedirect(uri: Uri) {
        try {
            // Extract token or auth data from the redirect URI
            val token = uri.getQueryParameter("token") ?: uri.getQueryParameter("auth_token")
            
            if (token != null) {
                // Return success with token
                val resultIntent = Intent().apply {
                    putExtra("token", token)
                }
                setResult(RESULT_OK, resultIntent)
            } else {
                // Authentication failed
                setResult(RESULT_CANCELED)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling auth redirect", e)
            setResult(RESULT_CANCELED)
        } finally {
            finish()
        }
    }
}