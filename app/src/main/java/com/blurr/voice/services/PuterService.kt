package com.blurr.voice.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import com.blurr.voice.R
import java.util.concurrent.CompletableFuture

class PuterService : Service() {
    private var webView: WebView? = null
    private val binder = PuterBinder()
    private var pendingAuthUrl: String? = null
    private var authCallback: ((String?) -> Unit)? = null
    private var signInCallback: ((Boolean) -> Unit)? = null
    private var authReceiver: BroadcastReceiver? = null

    companion object {
        const val TAG = "PuterService"
        const val PUTER_BRIDGE_READY = "puterBridgeReady"
        const val AUTH_REDIRECT_URI = "blurr://puter-auth-callback"
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
        registerAuthReceiver()
    }

    private fun registerAuthReceiver() {
        authReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    "AUTH_SUCCESS" -> {
                        val token = intent.getStringExtra("token")
                        Log.d(TAG, "Received auth success broadcast with token: $token")
                        // Handle successful authentication
                        handleAuthSuccess(token)
                    }
                    "AUTH_ERROR" -> {
                        val error = intent.getStringExtra("error")
                        Log.e(TAG, "Received auth error broadcast: $error")
                        // Handle authentication error
                        handleAuthError(error)
                    }
                }
            }
        }
        
        val filter = IntentFilter().apply {
            addAction("AUTH_SUCCESS")
            addAction("AUTH_ERROR")
        }
        
        registerReceiver(authReceiver, filter)
    }
    
    private fun handleAuthSuccess(token: String?) {
        if (token != null) {
            // Send the token to the WebView to complete authentication
            webView?.post {
                val jsCode = """
                    if (window.handleAuthCallback) {
                        // Complete the authentication in the Puter.js context
                        window.handleAuthCallback('$token');
                    } else if (window.puter && window.puter.auth) {
                        // Alternative method if handleAuthCallback is not available
                        console.log("handleAuthCallback not available, trying direct auth completion");
                    }
                """.trimIndent()
                webView?.evaluateJavascript(jsCode, null)
            }
            
            // Notify the sign in callback that authentication was successful
            signInCallback?.invoke(true)
            signInCallback = null
        }
    }
    
    private fun handleAuthError(error: String?) {
        Log.e(TAG, "Authentication error: $error")
        // Notify the sign in callback that authentication failed
        signInCallback?.invoke(false)
        signInCallback = null
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
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        val url = request?.url?.toString()
                        
                        Log.d(TAG, "Intercepting URL: $url")
                        
                        // Handle redirect back to app
                        if (url?.startsWith(AUTH_REDIRECT_URI) == true) {
                            Log.d(TAG, "Handling auth redirect: $url")
                            handleAuthRedirect(url)
                            return true
                        }
                        
                        // Intercept Puter.js authentication URL and redirect to Chrome Custom Tabs
                        // Mobile auth URL: https://puter.com/action/sign-in?embedded_in_popup=true&msg_id=1
                        // Desktop auth URL: https://puter.com/?embedded_in_popup=true&request_auth=true
                        if (url?.contains("puter.com/auth") == true || 
                            url?.contains("puter.com/login") == true || 
                            url?.contains("puter.com/action/sign-in") == true ||
                            url?.contains("puter.com/?embedded_in_popup=true") == true ||
                            url?.contains("puter.com/action/auth") == true ||
                            (url?.contains("puter.com") == true && 
                             (url?.contains("embedded_in_popup=true") == true || 
                              url?.contains("request_auth=true") == true ||
                              url?.contains("msg_id=") == true))) {
                            Log.d(TAG, "Opening auth in custom tab: $url")
                            openAuthInCustomTab(url)
                            return true
                        }
                        
                        return false
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d(TAG, "Page finished loading: $url")
                        // Inject Android interface after page loads
                        view?.addJavascriptInterface(AndroidInterface(), "AndroidInterface")
                        
                        // Check if this is the initial page load and send a ready signal
                        if (url?.contains("puter_webview.html") == true) {
                            Log.d(TAG, "Puter bridge ready")
                        }
                    }
                    
                    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        Log.d(TAG, "Page started loading: $url")
                        
                        // Check if this is an authentication-related page that might redirect
                        if (url?.contains("puter.com/auth") == true || 
                            url?.contains("puter.com/login") == true || 
                            url?.contains("puter.com/action/sign-in") == true) {
                            Log.d(TAG, "Authentication page loaded: $url")
                        }
                    }
                    
                    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                        super.onReceivedError(view, request, error)
                        Log.e(TAG, "WebView error: ${error?.description} for URL: ${request?.url}")
                    }
                    
                    // Override onReceivedHttpError to handle HTTP errors
                    override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                        super.onReceivedHttpError(view, request, errorResponse)
                        Log.e(TAG, "HTTP error: ${errorResponse?.statusCode} for URL: ${request?.url}")
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: android.os.Message?): Boolean {
                        // Intercept popup windows and redirect to Chrome Custom Tabs
                        val transport = resultMsg?.obj as? WebView.WebViewTransport
                        if (transport != null) {
                            val newWebView = transport.webView
                            
                            // Get the URL that would be loaded in the popup
                            val popupUrl = newWebView?.url
                            
                            // Close the popup WebView
                            newWebView?.destroy()
                            
                            // Open the URL in Chrome Custom Tabs instead
                            if (popupUrl != null && (popupUrl.contains("puter.com/auth") || 
                                    popupUrl.contains("puter.com/login") || 
                                    popupUrl.contains("puter.com/action/sign-in") ||
                                    popupUrl.contains("puter.com/?embedded_in_popup=true") ||
                                    (popupUrl.contains("puter.com") && 
                                     (popupUrl.contains("embedded_in_popup=true") || 
                                      popupUrl.contains("request_auth=true") ||
                                      popupUrl.contains("msg_id="))))) {
                                Log.d(TAG, "Opening popup auth in custom tab: $popupUrl")
                                openAuthInCustomTab(popupUrl)
                                return true
                            }
                        }
                        
                        return false
                    }
                    
                    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                        Toast.makeText(this@PuterService, message, Toast.LENGTH_SHORT).show()
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

    private fun openAuthInCustomTab(authUrl: String) {
        try {
            Log.d(TAG, "Opening authentication URL in custom tab: $authUrl")
            // Add the redirect URI to the auth URL to ensure proper redirect after authentication
            val uriBuilder = android.net.Uri.parse(authUrl).buildUpon()
            uriBuilder.appendQueryParameter("redirect_uri", AUTH_REDIRECT_URI)
            
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
            
            customTabsIntent.launchUrl(this, uriBuilder.build())
        } catch (e: Exception) {
            Log.e(TAG, "Error opening custom tab", e)
            Toast.makeText(this, "Failed to open authentication", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleAuthRedirect(redirectUrl: String) {
        Log.d(TAG, "Handling auth redirect: $redirectUrl")
        // Extract token or auth data from the redirect URL
        val token = extractTokenFromUrl(redirectUrl)
        Log.d(TAG, "Extracted token: $token")
        
        // Notify the sign in callback that authentication was successful
        signInCallback?.invoke(true)
        signInCallback = null
        
        // Also send a broadcast to handle the authentication success
        val intent = Intent("AUTH_SUCCESS")
        intent.putExtra("token", token)
        sendBroadcast(intent)
    }

    private fun extractTokenFromUrl(url: String): String? {
        // Extract token from URL parameters
        val uri = android.net.Uri.parse(url)
        return uri.getQueryParameter("token") ?: uri.getQueryParameter("auth_token") ?: 
               uri.getQueryParameter("access_token") ?: uri.getQueryParameter("auth_code")
    }

    fun executePuterChat(query: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterChat("$query")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "chat");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "chat");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterTxt2Img(prompt: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterTxt2Img("$prompt")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "txt2img");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "txt2img");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterImg2Txt(imageData: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterImg2Txt("$imageData")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "img2txt");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "img2txt");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterTxt2Speech(text: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterTxt2Speech("$text")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "txt2speech");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "txt2speech");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterKvGet(key: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterKvGet("$key")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "kvget");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "kvget");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterKvSet(key: String, value: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterKvSet("$key", "$value")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "kvset");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "kvset");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterKvDel(key: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterKvDel("$key")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "kvdeld");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "kvdeld");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterKvList(pattern: String = "*", returnValues: Boolean = false, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterKvList("$pattern", $returnValues)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "kvlist");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "kvlist");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterKvIncr(key: String, amount: Int = 1, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterKvIncr("$key", $amount)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "kvincr");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "kvincr");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterKvDecr(key: String, amount: Int = 1, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterKvDecr("$key", $amount)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "kvdecr");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "kvdecr");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterKvFlush(callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterKvFlush()
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "kvflush");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "kvflush");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterAuthSignIn(callback: (Boolean) -> Unit) {
        // Store the callback for later use
        signInCallback = callback

        webView?.post {
            val jsCode = """
                puterAuthSignIn()
                    .then(response => {
                        console.log("Sign in initiated successfully", response);
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAuthSuccess(JSON.stringify(response));
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
    
    fun executePuterChatStream(query: String, onChunkCallback: (String) -> Unit, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterChatStream("$query", (chunk) => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIResponse(JSON.stringify({type: 'chunk', data: chunk}), "chatstream");
                    }
                })
                .then(response => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIResponse(JSON.stringify({type: 'complete', data: response}), "chatstream");
                    }
                })
                .catch(error => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIError(error.message, "chatstream");
                    }
                });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterFsWrite(path: String, data: String, options: String = "{}", callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterFsWrite("$path", "$data", $options)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "fswrite");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "fswrite");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterFsRead(path: String, options: String = "{}", callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterFsRead("$path", $options)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "fsread");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "fsread");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterFsMkdir(path: String, options: String = "{}", callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterFsMkdir("$path", $options)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "fsmkdir");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "fsmkdir");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterFsReaddir(path: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterFsReaddir("$path")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "fsreaddir");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "fsreaddir");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterFsDelete(path: String, options: String = "{}", callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterFsDelete("$path", $options)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "fsdelete");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "fsdelete");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterFsMove(source: String, destination: String, options: String = "{}", callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterFsMove("$source", "$destination", $options)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "fsmove");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "fsmove");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterFsCopy(source: String, destination: String, options: String = "{}", callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterFsCopy("$source", "$destination", $options)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "fscopy");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "fscopy");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterFsRename(path: String, newName: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterFsRename("$path", "$newName")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "fsrename");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "fsrename");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterFsStat(path: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterFsStat("$path")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "fsstat");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "fsstat");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun executePuterFsSpace(callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterFsSpace()
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "fsspace");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "fsspace");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterGetTaskHistoryFromKvStore(callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterKvList("task_*", true)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "gettaskhistory");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "gettaskhistory");
                        }
                    });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }
    
    fun puterSaveTaskToKvStore(key: String, taskData: String, callback: (String?) -> Unit) {
        webView?.post {
            val jsCode = """
                puterKvSet("$key", "$taskData")
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), "savetasktostore");
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, "savetasktostore");
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
                "gettaskhistory" -> handleTaskHistoryResponse(response)
                "savetasktostore" -> handleTaskSaveResponse(response)
                "auth" -> {
                    // Handle authentication response
                    Log.d(TAG, "Received auth response: $response")
                    authCallback?.invoke(response)
                    authCallback = null
                }
                "authcheck" -> {
                    // Handle authentication check response
                    Log.d(TAG, "Received auth check response: $response")
                    authCallback?.invoke(response)
                    authCallback = null
                }
                "getuser" -> {
                    // Handle get user response
                    Log.d(TAG, "Received get user response: $response")
                    authCallback?.invoke(response)
                    authCallback = null
                }
                else -> {
                    Log.d(TAG, "Unhandled callbackId: $callbackId")
                    // For any other response, call the callback if it exists
                    authCallback?.invoke(response)
                    authCallback = null
                }
            }
        }

        @JavascriptInterface
        fun onAIError(error: String, callbackId: String) {
            Log.e(TAG, "Received error from Puter.js: $error, callbackId: $callbackId")
            // Handle the error based on callbackId
            when (callbackId) {
                "gettaskhistory" -> handleTaskHistoryError(error)
                "savetasktostore" -> handleTaskSaveError(error)
                "auth" -> {
                    // Handle authentication error
                    Log.e(TAG, "Auth error: $error")
                    authCallback?.invoke(null)
                    authCallback = null
                    
                    // Notify the sign in callback that authentication failed
                    signInCallback?.invoke(false)
                    signInCallback = null
                }
                "authcheck" -> {
                    // Handle authentication check error
                    Log.e(TAG, "Auth check error: $error")
                    authCallback?.invoke(null)
                    authCallback = null
                }
                "getuser" -> {
                    // Handle get user error
                    Log.e(TAG, "Get user error: $error")
                    authCallback?.invoke(null)
                    authCallback = null
                }
                else -> {
                    Log.d(TAG, "Unhandled error callbackId: $callbackId")
                    // For any other error, call the callback if it exists
                    authCallback?.invoke(null)
                    authCallback = null
                }
            }
        }

        @JavascriptInterface
        fun onAuthSuccess(userJson: String) {
            Log.d(TAG, "Authentication successful: $userJson")
            // Notify the app that authentication was successful
            authCallback?.invoke(userJson)
            authCallback = null
            
            // Notify the sign in callback that authentication was successful
            signInCallback?.invoke(true)
            signInCallback = null
        }
        
        private fun handleTaskHistoryResponse(response: String) {
            Log.d(TAG, "Task history response: $response")
        }
        
        private fun handleTaskSaveResponse(response: String) {
            Log.d(TAG, "Task save response: $response")
        }
        
        private fun handleTaskHistoryError(error: String) {
            Log.e(TAG, "Task history error: $error")
        }
        
        private fun handleTaskSaveError(error: String) {
            Log.e(TAG, "Task save error: $error")
        }
    }

    override fun onDestroy() {
        // Unregister the broadcast receiver
        authReceiver?.let { unregisterReceiver(it) }
        webView?.destroy()
        webView = null
        super.onDestroy()
    }
}