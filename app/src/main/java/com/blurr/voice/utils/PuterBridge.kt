package com.blurr.voice.utils

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.blurr.voice.services.PuterService
import org.json.JSONObject

class PuterBridge(private val puterService: PuterService) {
    companion object {
        const val TAG = "PuterBridge"
    }

    @JavascriptInterface
    fun onAIResponse(response: String, callbackId: String) {
        Log.d(TAG, "Received AI response: $response with callbackId: $callbackId")
        // Handle the response based on the callbackId
        // This would typically involve notifying the appropriate callback
    }

    @JavascriptInterface
    fun onAIError(error: String, callbackId: String) {
        Log.e(TAG, "Received AI error: $error with callbackId: $callbackId")
        // Handle the error based on the callbackId
    }

    @JavascriptInterface
    fun onAuthSuccess(userJson: String) {
        Log.d(TAG, "Authentication successful: $userJson")
        // Notify the app that authentication was successful
    }

    /**
     * Execute a Puter.js AI chat function
     */
    fun executeChat(webView: WebView, query: String, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js text-to-image function
     */
    fun executeTxt2Img(webView: WebView, prompt: String, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js image-to-text function
     */
    fun executeImg2Txt(webView: WebView, imageData: String, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js text-to-speech function
     */
    fun executeTxt2Speech(webView: WebView, text: String, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js key-value get function
     */
    fun executeKvGet(webView: WebView, key: String, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js key-value set function
     */
    fun executeKvSet(webView: WebView, key: String, value: String, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js authentication sign-in function
     */
    fun executeAuthSignIn(webView: WebView, callback: (Boolean) -> Unit) {
        val jsCode = """
            puterAuthSignIn()
                .then(response => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAuthSuccess(JSON.stringify(response));
                    }
                })
                .catch(error => {
                    if (window.AndroidInterface) {
                        window.AndroidInterface.onAIError(error.message, "auth");
                    }
                });
        """.trimIndent()
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js authentication check function
     */
    fun executeAuthCheck(webView: WebView, callback: (Boolean) -> Unit) {
        val jsCode = """
            var isSignedIn = puterAuthIsSignedIn();
            if (window.AndroidInterface) {
                window.AndroidInterface.onAIResponse(JSON.stringify(isSignedIn), "authcheck");
            }
        """.trimIndent()
        
        webView.evaluateJavascript(jsCode, null)
    }
}