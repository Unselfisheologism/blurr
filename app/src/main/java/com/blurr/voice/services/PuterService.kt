package com.blurr.voice.services

import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.webkit.*
import android.os.Message
import com.blurr.voice.LoginActivity

class PuterService : Service() {
    private var webView: WebView? = null
    private val binder = PuterBinder()
    var signInCallback: ((Boolean) -> Unit)? = null
    private val callbacks = mutableMapOf<String, (String?) -> Unit>()
    private var popupWebView: WebView? = null
    private var popupDialog: Dialog? = null

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
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                
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
                    // Set user agent to include "Mobile" for mobile-optimized experience
                    userAgentString = "$userAgentString PuterAndroidApp"
                }

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {  
                        val url = request?.url?.toString() ?: return false  
              
                        // This should no longer be needed since we're using popup WebViews
                        Log.d(TAG, "Main WebView loading URL: $url")
                        return false
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d(TAG, "Page finished loading: $url")
                        // Inject Android interface after page loads
                        view?.addJavascriptInterface(AndroidInterface(), "AndroidInterface")
                    }
                }

                webChromeClient = PuterWebChromeClient() // Use custom WebChromeClient
            }

            // Load the Puter bridge HTML
            webView?.loadUrl("file:///android_asset/puter_webview.html")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing WebView", e)
        }
    }
    
    // Custom WebChromeClient to handle popup windows
    inner class PuterWebChromeClient : WebChromeClient() {
        override fun onCreateWindow(
            view: WebView?,  
            isDialog: Boolean,  
            isUserGesture: Boolean,  
            resultMsg: Message?  
        ): Boolean {  
            Log.d(TAG, "onCreateWindow called - creating popup WebView")  
              
            // Create popup WebView  
            popupWebView = WebView(view?.context ?: return false).apply {  
                settings.apply {  
                    javaScriptEnabled = true  
                    domStorageEnabled = true  
                    databaseEnabled = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    setSupportMultipleWindows(true)  
                    javaScriptCanOpenWindowsAutomatically = true  
                    // Set user agent to include "Mobile" for mobile-optimized experience  
                    userAgentString = "$userAgentString PuterAndroidApp"  
                }  
                  
                // Set WebViewClient to handle navigation  
                webViewClient = object : WebViewClient() {  
                    override fun shouldOverrideUrlLoading(  
                        view: WebView?,  
                        request: WebResourceRequest?  
                    ): Boolean {  
                        val url = request?.url?.toString() ?: return false
                        Log.d(TAG, "Popup WebView loading: $url")  
                        
                        // Check if this is an authentication completion URL that should close the popup
                        if (url.contains("puter.com/auth-complete") || 
                            url.contains("puter.com/action/sign-in") || 
                            url.contains("embedded_in_popup=true") || 
                            url.contains("auth_callback") ||
                            url.contains("callback")) {
                            Log.d(TAG, "Detected auth completion URL, closing popup")
                            closePopup()
                            return true
                        }
                        
                        return false // Let WebView handle all other URLs  
                    }  
                      
                    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        Log.d(TAG, "Popup WebView page started: $url")
                        
                        // Check for authentication completion during page load
                        if (url?.contains("puter.com/auth-complete") == true || 
                            url?.contains("puter.com/action/sign-in") == true || 
                            url?.contains("embedded_in_popup=true") == true) {
                            Log.d(TAG, "Detected auth completion during page start, closing popup")
                            closePopup()
                        }
                    }
                      
                    override fun onPageFinished(view: WebView?, url: String?) {  
                        super.onPageFinished(view, url)  
                        Log.d(TAG, "Popup WebView finished loading: $url")  
                        
                        // Inject JavaScript to detect when authentication completes and close the popup
                        // This is especially important for the embedded popup scenario
                        view?.evaluateJavascript(
                            """
                            (function() {
                                console.log('Popup page finished, checking for auth completion');
                                
                                // Check for auth completion patterns in the current URL
                                if (window.location.href.includes('auth-complete') || 
                                    window.location.href.includes('callback') ||
                                    window.location.href.includes('token') ||
                                    window.location.href.includes('embedded_in_popup=true')) {
                                    
                                    console.log('Auth completion detected in URL, attempting to close popup');
                                    
                                    // Try multiple methods to close the popup
                                    if (window.close) {
                                        window.close();
                                    }
                                    
                                    // If window.close doesn't work, try sending a message to parent
                                    if (window.opener && window.opener.AndroidInterface) {
                                        window.opener.AndroidInterface.onAuthSuccess('{"completed": true}');
                                    }
                                }
                                
                                // Also set up a periodic check for auth completion
                                setInterval(function() {
                                    if (window.location.href.includes('auth-complete') || 
                                        window.location.href.includes('callback') ||
                                        window.location.href.includes('token')) {
                                        
                                        console.log('Periodic check found auth completion');
                                        
                                        if (window.close) {
                                            window.close();
                                        }
                                        
                                        if (window.opener && window.opener.AndroidInterface) {
                                            window.opener.AndroidInterface.onAuthSuccess(JSON.stringify({completed: true}));
                                        }
                                    }
                                }, 1000);
                                
                                // Listen for postMessage from puter.com
                                window.addEventListener('message', function(event) {
                                    console.log('Received message in popup:', event.data);
                                    
                                    if (event.data && (event.data.type === 'auth-callback' || 
                                                      event.data.type === 'puter-auth-callback' ||
                                                      event.data.token)) {
                                        console.log('Auth callback received, closing popup and notifying parent');
                                        
                                        if (window.opener && window.opener.AndroidInterface) {
                                            window.opener.AndroidInterface.onAuthSuccess(JSON.stringify(event.data));
                                        }
                                        
                                        if (window.close) {
                                            window.close();
                                        }
                                    }
                                });
                                
                                // Look for auth completion elements in the DOM
                                var observer = new MutationObserver(function(mutations) {
                                    mutations.forEach(function(mutation) {
                                        if (mutation.type === 'childList') {
                                            mutation.addedNodes.forEach(function(node) {
                                                if (node.textContent && (node.textContent.includes('auth-complete') || node.textContent.includes('Authentication successful'))) {
                                                    console.log('Auth completion detected in DOM changes');
                                                    
                                                    if (window.opener && window.opener.AndroidInterface) {
                                                        window.opener.AndroidInterface.onAuthSuccess(JSON.stringify({completed: true}));
                                                    }
                                                    
                                                    if (window.close) {
                                                        window.close();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                });
                                
                                observer.observe(document.body, { childList: true, subtree: true });
                            })();
                            """, 
                            null
                        )
                    }
                    
                    private fun closePopup() {
                        // Close the popup dialog and destroy the WebView
                        popupDialog?.dismiss()
                        popupWebView?.destroy()
                        popupWebView = null
                    }
                }  
                  
                // Set WebChromeClient for the popup  
                webChromeClient = object : WebChromeClient() {  
                    override fun onCloseWindow(window: WebView?) {  
                        Log.d(TAG, "onCloseWindow called for popup")  
                        popupDialog?.dismiss()  
                        popupWebView?.destroy()  
                        popupWebView = null  
                    }  
                }  
            }  
              
            // Create dialog to display popup WebView with null safety
            popupWebView?.let { webView ->
                popupDialog = Dialog(this@PuterService, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {  
                    requestWindowFeature(Window.FEATURE_NO_TITLE)  
                    setContentView(webView)  
                    setCancelable(true)  
                    setOnCancelListener {  
                        Log.d(TAG, "Popup dialog cancelled")  
                        // Notify the main WebView that authentication was cancelled
                        webView.evaluateJavascript(
                            """
                            if (window.opener && window.opener.AndroidInterface) {
                                window.opener.AndroidInterface.onAuthError('Authentication cancelled by user');
                            }
                            """, 
                            null
                        )
                        popupWebView?.destroy()  
                        popupWebView = null  
                    }  
                    show()  
                }
            }
              
            // Send the WebView to the message  
            val transport = resultMsg?.obj as? WebView.WebViewTransport  
            transport?.webView = popupWebView  
            resultMsg?.sendToTarget()  
              
            return true  
        }  
          
        override fun onCloseWindow(window: WebView?) {  
            Log.d(TAG, "onCloseWindow called for main WebView")  
            popupDialog?.dismiss()  
            popupWebView?.destroy()  
            popupWebView = null  
        }  
    }

    // Chat functionality
    fun executePuterChat(query: String, callback: (String?) -> Unit) {
        val callbackId = "chat_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // Text to image functionality
    fun executePuterTxt2Img(prompt: String, callback: (String?) -> Unit) {
        val callbackId = "txt2img_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // Image to text functionality
    fun executePuterImg2Txt(imageData: String, callback: (String?) -> Unit) {
        val callbackId = "img2txt_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // Text to speech functionality
    fun executePuterTxt2Speech(text: String, callback: (String?) -> Unit) {
        val callbackId = "txt2speech_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store get functionality
    fun puterKvGet(key: String, callback: (String?) -> Unit) {
        val callbackId = "kvget_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store set functionality
    fun puterKvSet(key: String, value: String, callback: (String?) -> Unit) {
        val callbackId = "kvset_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store delete functionality
    fun puterKvDel(key: String, callback: (String?) -> Unit) {
        val callbackId = "kvdeld_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store list functionality
    fun puterKvList(pattern: String, returnValues: Boolean, callback: (String?) -> Unit) {
        val callbackId = "kvlist_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store increment functionality
    fun puterKvIncr(key: String, amount: Int, callback: (String?) -> Unit) {
        val callbackId = "kvincr_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store decrement functionality
    fun puterKvDecr(key: String, amount: Int, callback: (String?) -> Unit) {
        val callbackId = "kvdecr_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // KV store flush functionality
    fun puterKvFlush(callback: (String?) -> Unit) {
        val callbackId = "kvflush_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // Chat streaming functionality
    fun executePuterChatStream(query: String, onChunkCallback: (String) -> Unit, callback: (String?) -> Unit) {
        val callbackId = "chatstream_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system write functionality
    fun executePuterFsWrite(path: String, data: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fswrite_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system read functionality
    fun executePuterFsRead(path: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fsread_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system mkdir functionality
    fun executePuterFsMkdir(path: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fsmkdir_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system readdir functionality
    fun executePuterFsReaddir(path: String, callback: (String?) -> Unit) {
        val callbackId = "fsreaddir_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system delete functionality
    fun executePuterFsDelete(path: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fsdelete_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system move functionality
    fun executePuterFsMove(source: String, destination: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fsmove_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system copy functionality
    fun executePuterFsCopy(source: String, destination: String, optionsJson: String, callback: (String?) -> Unit) {
        val callbackId = "fscopy_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system rename functionality
    fun executePuterFsRename(path: String, newName: String, callback: (String?) -> Unit) {
        val callbackId = "fsrename_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system stat functionality
    fun executePuterFsStat(path: String, callback: (String?) -> Unit) {
        val callbackId = "fsstat_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // File system space functionality
    fun executePuterFsSpace(callback: (String?) -> Unit) {
        val callbackId = "fsspace_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // Task history functionality
    fun puterGetTaskHistoryFromKvStore(callback: (String?) -> Unit) {
        val callbackId = "taskhistory_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }

    // Save task to KV store functionality
    fun puterSaveTaskToKvStore(key: String, taskDataJson: String, callback: (String?) -> Unit) {
        val callbackId = "savetask_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
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
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }
    // Authentication check if signed in functionality
    fun puterAuthIsSignedIn(callback: (Boolean) -> Unit) {
        val callbackId = "authcheck_" + System.currentTimeMillis()
        callbacks[callbackId] = { response ->
            try {
                val signedIn = response?.let { 
                    val jsonResponse = org.json.JSONObject(it)
                    jsonResponse.optBoolean("signedIn", false)
                } ?: false
                callback(signedIn)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing auth check response", e)
                callback(false)
            }
        }
        webView?.post {
            val jsCode = """
                puter.auth.isSignedIn().then(signedIn => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIResponse(JSON.stringify({signedIn: signedIn}), '$callbackId');
                    }
                }).catch(error => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIError(error.message, '$callbackId');
                    }
                });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }
    
    // Authentication get user functionality
    fun puterAuthGetUser(callback: (String?) -> Unit) {
        val callbackId = "getuser_" + System.currentTimeMillis()
        callbacks[callbackId] = callback
        webView?.post {
            val jsCode = """
                puter.auth.getUser().then(user => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIResponse(JSON.stringify(user), '$callbackId');
                    }
                }).catch(error => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIError(error.message, '$callbackId');
                    }
                });
            """.trimIndent()
            
            webView?.evaluateJavascript(jsCode, null)
        }
    }
    
    // Initialize Puter with an authentication token
    fun injectAuthToken(token: String) {  
        webView?.post {  
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
          
            webView?.evaluateJavascript(jsCode) { result ->  
                Log.d(TAG, "Token injection result: $result")  
            }  
        }  
    }

    // Method to evaluate arbitrary JavaScript in the WebView
    fun evaluateJavascript(jsCode: String, resultCallback: ValueCallback<String>?) {
        webView?.post {
            webView?.evaluateJavascript(jsCode, resultCallback)
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
        fun onAuthError(error: String) {
            Log.e(TAG, "Received auth error: $error")
            // This method will be called when authentication fails
            signInCallback?.invoke(false)
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
