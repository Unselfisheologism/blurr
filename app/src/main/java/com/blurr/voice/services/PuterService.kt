package com.blurr.voice.services

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.webkit.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.blurr.voice.LoginActivity

class PuterService : Service() {
    private var webView: WebView? = null
    private val binder = PuterBinder()
    private var signInCallback: ((Boolean) -> Unit)? = null
    private var authUrlCallback: ((String) -> Unit)? = null

    companion object {
        const val TAG = "PuterService"
    }

    inner class PuterBinder : Binder() {
        fun getService(): PuterService = this@PuterService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        initializeWebView()
    }

    private fun initializeWebView() {
        try {
            webView = WebView(this).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    setSupportMultipleWindows(true)
                    javaScriptCanOpenWindowsAutomatically = true
                    // Enable DOM storage for better persistence
                    domStorageEnabled = true
                    // Enable database for better persistence
                    databaseEnabled = true
                    // Enable cache mode
                    cacheMode = WebSettings.LOAD_DEFAULT
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d(TAG, "Page finished loading: $url")
                        // Inject Android interface after page loads
                        view?.addJavascriptInterface(AndroidInterface(), "AndroidInterface")
                    }
                    
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        val url = request?.url.toString()
                        
                        // Detect Puter authentication initiation
                        if (url.contains("puter.com") && (url.contains("auth") || url.contains("action/sign-in") || url.contains("embedded_in_popup"))) {
                            // This is where we intercept and redirect to Chrome Custom Tabs
                            Log.d(TAG, "Authentication URL detected: $url")
                            authUrlCallback?.invoke(url)
                            return true // Prevent WebView from loading this URL
                        }
                        
                        // Handle the redirect back to the app after auth
                        if (url.startsWith("blurr://puter-auth-callback")) {
                            Log.d(TAG, "Auth callback detected: $url")
                            // Extract token from URL and complete authentication
                            val token = Uri.parse(url).getQueryParameter("token")
                            if (!token.isNullOrEmpty()) {
                                // Send token back to WebView to complete authentication
                                webView?.post {
                                    val jsCode = "handleAuthCallback('$token');"
                                    webView?.evaluateJavascript(jsCode, null)
                                }
                                // Notify that authentication was successful
                                signInCallback?.invoke(true)
                                signInCallback = null
                            }
                            return true
                        }
                        
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                        android.widget.Toast.makeText(this@PuterService, message, android.widget.Toast.LENGTH_SHORT).show()
                        result?.confirm()
                        return true
                    }
                }
            }

            // Load the Puter bridge HTML
            webView?.loadUrl("file:///android_asset/puter_webview.html")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing WebView", e)
        }
    }

    fun puterAuthSignIn(callback: (Boolean) -> Unit) {
        // Store the callback for later use
        signInCallback = callback

        // Execute the JavaScript function to trigger authentication
        webView?.post {
            val jsCode = """
                puterAuthSignIn()
                    .then(result => {
                        console.log("Puter auth sign initiated:", result);
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(result), "authsignin");
                        }
                    })
                    .catch(error => {
                        console.error("Puter auth sign in error:", error);
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "authsignin");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun setAuthUrlCallback(callback: (String) -> Unit) {
        authUrlCallback = callback
    }

    fun puterAuthIsSignedIn(callback: (Boolean) -> Unit) {
        webView?.post {
            val jsCode = """
                var isSignedIn = puterAuthIsSignedIn();
                if (window.AndroidInterface) {
                    window.AndroidInterface.onAIResponse(JSON.stringify(isSignedIn), "authcheck");
                }
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterAuthGetUser(callback: (String?) -> Unit) {
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
    }
    
    inner class AndroidInterface {
        @JavascriptInterface
        fun onAIResponse(response: String, callbackId: String) {
            Log.d(TAG, "Received response from Puter.js: $response, callbackId: $callbackId")
            // Handle the response based on callbackId
            when (callbackId) {
                "authcheck" -> {
                    // Handle authentication check response
                    Log.d(TAG, "Received auth check response: $response")
                    try {
                        val jsonResponse = org.json.JSONObject(response)
                        val signedIn = jsonResponse.getBoolean("signedIn")
                        signInCallback?.invoke(signedIn)
                        signInCallback = null
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing auth check response", e)
                        signInCallback?.invoke(false)
                        signInCallback = null
                    }
                }
                "getuser" -> {
                    // Handle get user response
                    Log.d(TAG, "Received get user response: $response")
                    // For get user response, we don't need to do anything special
                }
                "authsignin" -> {
                    // Handle authentication sign in response
                    Log.d(TAG, "Received auth sign in response: $response")
                    try {
                        val jsonResponse = org.json.JSONObject(response)
                        val status = jsonResponse.getString("status")
                        signInCallback?.invoke(status == "auth_started")
                        signInCallback = null
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing auth sign in response", e)
                        signInCallback?.invoke(false)
                        signInCallback = null
                    }
                }
                else -> {
                    Log.d(TAG, "Unhandled callbackId: $callbackId")
                }
            }
        }

        @JavascriptInterface
        fun onAIError(error: String, callbackId: String) {
            Log.e(TAG, "Received error from Puter.js: $error, callbackId: $callbackId")
            // Handle the error based on callbackId
            when (callbackId) {
                "authcheck" -> {
                    // Handle authentication check error
                    Log.e(TAG, "Auth check error: $error")
                    signInCallback?.invoke(false)
                    signInCallback = null
                }
                "getuser" -> {
                    // Handle get user error
                    Log.e(TAG, "Get user error: $error")
                }
                "authsignin" -> {
                    // Handle authentication sign in error
                    Log.e(TAG, "Auth sign in error: $error")
                    signInCallback?.invoke(false)
                    signInCallback = null
                }
                else -> {
                    Log.d(TAG, "Unhandled error callbackId: $callbackId")
                }
            }
        }
        
        @JavascriptInterface
        fun onAuthUrlRequested(url: String) {
            Log.d(TAG, "Received auth URL request: $url")
            // This method will be called when the JavaScript requests an auth URL
            // to be handled by Android Custom Tabs
            // The URL should be passed to the LoginActivity to handle with Custom Tabs
            authUrlCallback?.invoke(url)
        }
        
        @JavascriptInterface
        fun onAuthSuccess(response: String) {
            Log.d(TAG, "Received auth success: $response")
            // This method will be called when authentication is complete
            signInCallback?.invoke(true)
            signInCallback = null
        }
        
        @JavascriptInterface
        fun handleAuthResponse(token: String) {
            Log.d(TAG, "Received auth response from JavaScript: $token")
            // This method is called by JavaScript when authentication is completed
            // and the token is available
            signInCallback?.invoke(true)
            signInCallback = null
        }
    }

    override fun onDestroy() {
        webView?.destroy()
        webView = null
        super.onDestroy()
    }
}