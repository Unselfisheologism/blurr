package com.blurr.voice

import android.app.Dialog
import android.content.Context
import android.os.Message
import android.view.View
import android.webkit.*

class PuterWebChromeClient : WebChromeClient() {
    private var popupWebView: WebView? = null
    private var popupDialog: Dialog? = null
    
    override fun onCreateWindow(
        view: WebView,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message
    ): Boolean {
        // Create popup WebView with all required settings
        popupWebView = WebView(view.context).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                setSupportMultipleWindows(true) // Essential for popup windows
                javaScriptCanOpenWindowsAutomatically = true  // Must be true
                allowFileAccess = true
                allowContentAccess = true
                databaseEnabled = true
                // Set a proper user agent to ensure mobile-optimized experience
                userAgentString = "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.0 Mobile Safari/537.36 PuterApp"
            }
            
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    return false // Let WebView handle all URLs
                }
            }
            
            webChromeClient = object : WebChromeClient() {
                override fun onCloseWindow(window: WebView) {
                    popupDialog?.dismiss()
                    popupWebView?.destroy()
                    popupWebView = null
                }
            }
        }
        
        // Check if popupWebView is not null before proceeding
        if (popupWebView == null) {
            return false
        }
        
        // Create dialog to display popup WebView
        popupDialog = Dialog(view.context, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
            setContentView(popupWebView!!) // Safe to use !! since we checked for null above
            setCancelable(true)
            setOnCancelListener {
                popupWebView?.destroy()
                popupWebView = null
            }
            show()
        }
        
        // Send the WebView to the message
        val transport = resultMsg.obj as WebView.WebViewTransport
        transport.webView = popupWebView
        resultMsg.sendToTarget()
        
        return true
    }
    
    override fun onCloseWindow(window: WebView) {
        popupDialog?.dismiss()
        popupWebView?.destroy()
        popupWebView = null
    }
}