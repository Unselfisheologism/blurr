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
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        popupWebView = WebView(view?.context ?: return false).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return false
                }
            }
            
            webChromeClient = object : WebChromeClient() {
                override fun onCloseWindow(window: WebView?) {
                    popupDialog?.dismiss()
                    popupWebView?.destroy()
                    popupWebView = null
                }
            }
        }
        
        popupDialog = Dialog(view?.context ?: return false, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
            popupWebView?.let { setContentView(it) }
            setCancelable(true)
            setOnCancelListener {
                popupWebView?.destroy()
                popupWebView = null
            }
            show()
        }
        
        val transport = resultMsg?.obj as? WebView.WebViewTransport
        transport?.webView = popupWebView
        resultMsg?.sendToTarget()
        
        return true
    }
    
    override fun onCloseWindow(window: WebView?) {
        popupDialog?.dismiss()
        popupWebView?.destroy()
        popupWebView = null
    }
}