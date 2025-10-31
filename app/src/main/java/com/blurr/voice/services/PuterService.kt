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
    private var WebView: WebView? = null
    private val binder = PuterBinder()
    private var signInCallback: ((Boolean) -> Unit)? = null
    private var authUrlCallback: ((String) -> Unit)? = null
    private val callbacks = mutableMapOf<String, (String?) -> Unit>()

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
            WebView = WebView(this).apply {
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
                    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {  
                        val url = request.url.toString() ?: return false  
      
                        // Intercept Puter.js authentication URLs  
                        if (url.contains("puter.com/action/sign-in") ||   
                            url.contains("puter.com/?embedded_in_popup=true") ||  
                            url.contains("request_auth=true")) {  
          
                           Log.d(TAG, "Intercepting auth URL from puter.auth.signIn(): $url")  
          
                           // Launch Chrome Custom Tabs instead  
                           authUrlCallback?.invoke(url)  
          
                           // Prevent WebView from loading this URL  
                           return true  
                        }  
      
                        return false  
                    }

                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)
                        Log.d(TAG, "Page finished loading: $url")
                        // Inject Android interface after page loads
                        view.addJavascriptInterface(AndroidInterface(), "AndroidInterface")
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onJsAlert(view: WebView, url: String?, message: String?, result: JsResult): Boolean {
                        android.widget.Toast.makeText(this@PuterService, message, android.widget.Toast.LENGTH_SHORT).show()
                        result?.confirm()
                        return true
                    }
                }
            }

            // Load the Puter bridge HTML
            WebView?.loadUrl("file:///android_asset/puter_WebView.html")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing WebView", e)
        }
    }

    fun setAuthUrlCallback(callback: (String) -> Unit) {
        authUrlCallback = callback
    }
    
    fun puterAuthIsSignedIn(callback: (Boolean) -> Unit) {
        WebView?.post {
            val jsCode = """
                var isSignedIn = puterAuthIsSignedIn();
                if (window.AndroidInterface) {
                    window.AndroidInterface.onAIResponse(JSON.stringify(isSignedIn), "authcheck");
                }
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    fun puterAuthGetUser(callback: (String?) -> Unit) {
        WebView?.post {
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
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    fun injectAuthToken(token: String) {  
        WebView?.post {  
            val jsCode = """  
                (function() {  
                    // Store token in localStorage for Puter.js to use  
                    localStorage.setItem('puter_auth_token', '$token');  
                  
                    // If Puter.js has a method to set auth token, call it  
                    if (typeof puter !== 'undefined' && puter.auth && puter.auth.setToken) {  
                        puter.auth.setToken('$token');  
                    }  
                  
                    // Notify that authentication is complete  
                    if (window.AndroidInterface) {  
                        window.AndroidInterface.onAuthSuccess('{"success": true}');  
                    }  
                })();  
            """.trimIndent()  
          
            WebView?.evaluateJavascript(jsCode) { result ->  
                Log.d(TAG, "Token injection result: $result")  
            }  
        }  
    }

    // Chat functionality
    fun executePuterChat(query: String, callback: (String?) -> Unit) {
        val callbackId = "chat_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterChat('$query')
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // Text to image functionality
    fun executePuterTxt2Img(prompt: String, callback: (String?) -> Unit) {
        val callbackId = "txt2img_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterTxt2Img('$prompt')
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // Image to text functionality
    fun executePuterImg2Txt(imageData: String, callback: (String?) -> Unit) {
        val callbackId = "img2txt_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterImg2Txt('$imageData')
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // Text to speech functionality
    fun executePuterTxt2Speech(text: String, callback: (String?) -> Unit) {
        val callbackId = "txt2speech_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterTxt2Speech('$text')
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store get functionality
    fun puterKvGet(key: String, callback: (String?) -> Unit) {
        val callbackId = "kvget_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterKvGet('$key')
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store set functionality
    fun puterKvSet(key: String, value: String, callback: (String?) -> Unit) {
        val callbackId = "kvset_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterKvSet('$key', '$value')
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store delete functionality
    fun puterKvDel(key: String, callback: (String?) -> Unit) {
        val callbackId = "kvdeld_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterKvDel('$key')
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store list functionality
    fun puterKvList(pattern: String, returnValues: Boolean, callback: (String?) -> Unit) {
        val callbackId = "kvlist_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterKvList('$pattern', $returnValues)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store increment functionality
    fun puterKvIncr(key: String, amount: Int, callback: (String?) -> Unit) {
        val callbackId = "kvincr_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterKvIncr('$key', $amount)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store decrement functionality
    fun puterKvDecr(key: String, amount: Int, callback: (String?) -> Unit) {
        val callbackId = "kvdecr_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterKvDecr('$key', $amount)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store flush functionality
    fun puterKvFlush(callback: (String?) -> Unit) {
        val callbackId = "kvflush_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterKvFlush()
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // Chat streaming functionality
    fun executePuterChatStream(query: String, onChunkCallback: (String) -> Unit, callback: (String?) -> Unit) {
        val callbackId = "chatstream_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterChatStream('$query', function(chunk) {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIResponse(JSON.stringify({type: 'chunk', data: chunk}), '$callbackId');
                    }
                })
                .then(response => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIResponse(JSON.stringify({type: 'complete', data: response}), '$callbackId');
                    }
                })
                .catch(error => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIError(error.message, '$callbackId');
                    }
                });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system write functionality
    fun executePuterFsWrite(path: String, data: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fswrite_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterFsWrite('$path', '$data', JSON.parse('$optionsJson'))
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system read functionality
    fun executePuterFsRead(path: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fsread_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterFsRead('$path', JSON.parse('$optionsJson'))
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system mkdir functionality
    fun executePuterFsMkdir(path: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fsmkdir_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterFsMkdir('$path', JSON.parse('$optionsJson'))
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system readdir functionality
    fun executePuterFsReaddir(path: String, callback: (String?) -> Unit) {
        val callbackId = "fsreaddir_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterFsReaddir('$path')
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system delete functionality
    fun executePuterFsDelete(path: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fsdelete_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterFsDelete('$path', JSON.parse('$optionsJson'))
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system move functionality
    fun executePuterFsMove(source: String, destination: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fsmove_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterFsMove('$source', '$destination', JSON.parse('$optionsJson'))
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system copy functionality
    fun executePuterFsCopy(source: String, destination: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fscopy_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterFsCopy('$source', '$destination', JSON.parse('$optionsJson'))
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system rename functionality
    fun executePuterFsRename(path: String, newName: String, callback: (String?) -> Unit) {
        val callbackId = "fsrename_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterFsRename('$path', '$newName')
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system stat functionality
    fun executePuterFsStat(path: String, callback: (String?) -> Unit) {
        val callbackId = "fsstat_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterFsStat('$path')
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system space functionality
    fun executePuterFsSpace(callback: (String?) -> Unit) {
        val callbackId = "fsspace_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterFsSpace()
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // Task history functionality
    fun puterGetTaskHistoryFromKvStore(callback: (String?) -> Unit) {
        val callbackId = "taskhistory_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterKvList('task_*', true)
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }

    // Save task to KV store functionality
    fun puterSaveTaskToKvStore(key: String, taskDataJson: String, callback: (String?) -> Unit) {
        val callbackId = "savetask_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        WebView?.post {
            val jsCode = """
                puterKvSet('$key', JSON.parse('$taskDataJson'))
                    .then(response => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIResponse(JSON.stringify(response), '$callbackId');
                        }
                    })
                    .catch(error => {
                        if (window.AndroidInterface) {
                            window.AndroidInterface.onAIError(error.message, '$callbackId');
                        }
                    });
            """.trimIndent()
            
            WebView?.evaluateJavascript(jsCode, null)
        }
    }
    // Method to evaluate arbitrary JavaScript in the WebView
    fun evaluateJavascript(jsCode: String, resultCallback: ValueCallback<String>?) {
        WebView?.post {
            WebView?.evaluateJavascript(jsCode, resultCallback)
        }
    }
    
    // Helper method to find a callback ID by its type/prefix
    private fun findCallbackIdByType(type: String): String? {
        return callbacks.keys.find { it.startsWith(type) }
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
                else -> {
                    Log.d(TAG, "Received response for callback: $callbackId, response: $response")
                    // For all responses, call the callback if it exists using the callbackId directly
                    callbacks.remove(callbackId)?.invoke(response)
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
                else -> {
                    Log.e(TAG, "Received error for callback: $callbackId, error: $error")
                    // For all errors, call the callback if it exists using the callbackId directly
                    callbacks.remove(callbackId)?.invoke(null)
                }
            }
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
        WebView?.destroy()
        WebView = null
        super.onDestroy()
    }
}