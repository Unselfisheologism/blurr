package com.blurr.voice

import android.app.Dialog
import android.content.Context
import android.os.Message
import android.view.ViewGroup
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
                domStorageEnabled = true
                // Set a proper user agent to ensure mobile-optimized experience
                userAgentString = "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.0 Mobile Safari/537.36 PuterApp"
            }
             
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    // Allow all URLs to be loaded in the popup WebView
                    return false
                }
            }
             
            webChromeClient = object : WebChromeClient() {
                override fun onCloseWindow(window: WebView) {
                    // Properly close the popup dialog
                    popupDialog?.dismiss()
                    popupWebView?.destroy()
                    popupWebView = null
                    popupDialog = null
                }
            }
        }
         
        // Create dialog to display popup WebView
        popupDialog = Dialog(view.context, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
            setContentView(popupWebView!!)
            setCancelable(true)
            setOnCancelListener {
                // Clean up when dialog is cancelled
                popupWebView?.destroy()
                popupWebView = null
                popupDialog = null
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
        // Ensure proper cleanup when window is closed
        popupDialog?.dismiss()
        popupWebView?.destroy()
        popupWebView = null
        popupDialog = null
    }
}