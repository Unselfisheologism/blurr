package com.blurr.voice.utils

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import java.util.concurrent.CompletableFuture

/**
 * PuterBridge provides a global interface to the Puter.js functionality
 * This class ensures that Puter.js is properly loaded and available throughout the app
 */
class PuterBridge(private val context: Context) {
    private var webView: WebView? = null
    private val callbacks = mutableMapOf<String, (String?) -> Unit>()
    private var callbackCounter = 0
    private val TAG = "PuterBridge"
    
    companion object {
        @Volatile
        private var INSTANCE: PuterBridge? = null
        
        fun getInstance(context: Context): PuterBridge {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PuterBridge(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    init {
        initializeWebView()
    }
    
    private fun initializeWebView() {
        try {
            webView = WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    setSupportMultipleWindows(true)
                    javaScriptCanOpenWindowsAutomatically = true
                    // Enable DOM storage for better persistence
                    domStorageEnabled = true
                    // Enable database for better persistence
                    databaseEnabled = true
                    // Enable cache mode
                    cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
                }
                
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d(TAG, "Puter.js bridge page finished loading: $url")
                        // Inject Android interface after page loads
                        view?.addJavascriptInterface(AndroidInterface(), "AndroidInterface")
                    }
                    
                    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        Log.d(TAG, "Puter.js bridge page started loading: $url")
                    }
                    
                    override fun onReceivedError(view: WebView?, request: android.webkit.WebResourceRequest?, error: android.webkit.WebResourceError?) {
                        super.onReceivedError(view, request, error)
                        Log.e(TAG, "Puter.js bridge WebView error: ${error?.description} for URL: ${request?.url}")
                    }
                }
                
                webChromeClient = object : android.webkit.WebChromeClient() {
                    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: android.webkit.JsResult?): Boolean {
                        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                        result?.confirm()
                        return true
                    }
                }
            }
            
            // Load the Puter bridge HTML
            webView?.loadUrl("file:///android_asset/puter_webview.html")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Puter.js WebView bridge", e)
        }
    }
    
    private fun getNextCallbackId(): String {
        return "callback_${callbackCounter++}"
    }
    
    // Authentication functions
    fun signIn(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val callbackId = getNextCallbackId()
        
        callbacks[callbackId] = { response ->
            future.complete(response?.toBoolean() ?: false)
        }
        
        webView?.post {
            val jsCode = """
                puterAuthSignIn()
                    .then(response => {
                        console.log("Sign in initiated successfully", response);
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "auth");
                        }
                    })
                    .catch(error => {
                        console.error("Sign in error", error);
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "auth");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
        
        return future
    }
    
    fun isSignedIn(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val callbackId = getNextCallbackId()
        
        callbacks[callbackId] = { response ->
            future.complete(response?.toBoolean() ?: false)
        }
        
        webView?.post {
            val jsCode = """
                var isSignedIn = puterAuthIsSignedIn();
                if (window.AndroidInterface) {
                    window.AndroidInterface.onAIResponse(JSON.stringify(isSignedIn), "authcheck");
                }
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
        
        return future
    }
    
    fun getUser(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val callbackId = getNextCallbackId()
        
        callbacks[callbackId] = { response ->
            future.complete(response ?: "")
        }
        
        webView?.post {
            val jsCode = """
                puterAuthGetUser()
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "getuser");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "getuser");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
        
        return future
    }
    
    fun getUserName(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        getUser().thenApply { userJson ->
            try {
                val user = org.json.JSONObject(userJson)
                val name = user.optString("username", "Unknown")
                future.complete(name)
            } catch (e: Exception) {
                future.complete("Unknown")
            }
        }.exceptionally { throwable ->
            future.complete("Unknown")
            null
        }
        return future
    }
    
    fun getUserEmail(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        getUser().thenApply { userJson ->
            try {
                val user = org.json.JSONObject(userJson)
                val email = user.optString("email", "unknown@example.com")
                future.complete(email)
            } catch (e: Exception) {
                future.complete("unknown@example.com")
            }
        }.exceptionally { throwable ->
            future.complete("unknown@example.com")
            null
        }
        return future
    }
    
    inner class AndroidInterface {
        @JavascriptInterface
        fun onAIResponse(response: String, callbackId: String) {
            Log.d(TAG, "Received response from Puter.js: $response, callbackId: $callbackId")
            // Handle the response based on callbackId
            when (callbackId) {
                "auth" -> {
                    // Handle authentication response
                    Log.d(TAG, "Received auth response: $response")
                    callbacks.remove(callbackId)?.invoke(response)
                }
                "authcheck" -> {
                    // Handle authentication check response
                    Log.d(TAG, "Received auth check response: $response")
                    callbacks.remove(callbackId)?.invoke(response)
                }
                "getuser" -> {
                    // Handle get user response
                    Log.d(TAG, "Received get user response: $response")
                    callbacks.remove(callbackId)?.invoke(response)
                }
                else -> {
                    Log.d(TAG, "Unhandled callbackId: $callbackId")
                    // For any other response, call the callback if it exists
                    callbacks.remove(callbackId)?.invoke(response)
                }
            }
        }
        
        @JavascriptInterface
        fun onAIError(error: String, callbackId: String) {
            Log.e(TAG, "Received error from Puter.js: $error, callbackId: $callbackId")
            // Handle the error based on callbackId
            when (callbackId) {
                "auth" -> {
                    // Handle authentication error
                    Log.e(TAG, "Auth error: $error")
                    callbacks.remove(callbackId)?.invoke(null)
                }
                "authcheck" -> {
                    // Handle authentication check error
                    Log.e(TAG, "Auth check error: $error")
                    callbacks.remove(callbackId)?.invoke(null)
                }
                "getuser" -> {
                    // Handle get user error
                    Log.e(TAG, "Get user error: $error")
                    callbacks.remove(callbackId)?.invoke(null)
                }
                else -> {
                    Log.d(TAG, "Unhandled error callbackId: $callbackId")
                    // For any other error, call the callback if it exists
                    callbacks.remove(callbackId)?.invoke(null)
                }
            }
        }
    }
    
    fun shutdown() {
        webView?.destroy()
        webView = null
    }
}