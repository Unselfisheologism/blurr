package com.blurr.voice

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Message
import android.util.Log
import android.webkit.*
import androidx.browser.customtabs.CustomTabsIntent
import com.blurr.voice.activities.PuterAuthCallbackActivity

class PuterWebChromeClient : WebChromeClient() {
    private val TAG = "PuterWebChromeClient"
     
    override fun onCreateWindow(
        view: WebView,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message
    ): Boolean {
        Log.d(TAG, "onCreateWindow called - checking if this is an authentication request")
        
        // Check if this is an authentication request by accessing the WebView's current URL
        val currentUrl = view.url
        Log.d(TAG, "Current WebView URL: $currentUrl")
        
        // Check if this is a Puter authentication request
        if (currentUrl?.contains("puter.com") == true || currentUrl?.contains("puter.dev") == true) {
            // This is likely an authentication request, so we should intercept it
            Log.d(TAG, "Detected Puter authentication request, opening in Chrome Custom Tab")
            
            // Instead of creating a popup window, open the URL in Chrome Custom Tab
            // We need to get the URL that would be opened in the popup
            // This requires checking the pending JavaScript execution that triggered this method
            // For now, we'll handle it in the WebViewClient instead
            
            // Return false to allow normal processing but we'll intercept it in WebViewClient
            return false
        }
        
        // For non-authentication popups, continue with the original behavior if needed
        // But for Puter.js, we want to handle authentication specially
        Log.d(TAG, "Not an authentication request, returning false")
        return false
    }
     
    override fun onCloseWindow(window: WebView) {
        Log.d(TAG, "onCloseWindow called")
        // Cleanup when window closes
    }
}