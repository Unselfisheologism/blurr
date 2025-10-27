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
     * Execute a Puter.js key-value delete function
     */
    fun executeKvDel(webView: WebView, key: String, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js key-value list function
     */
    fun executeKvList(webView: WebView, pattern: String = "*", returnValues: Boolean = false, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js key-value increment function
     */
    fun executeKvIncr(webView: WebView, key: String, amount: Int = 1, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js key-value decrement function
     */
    fun executeKvDecr(webView: WebView, key: String, amount: Int = 1, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js key-value flush function
     */
    fun executeKvFlush(webView: WebView, callback: (String?) -> Unit) {
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

    /**
     * Execute a Puter.js get user function
     */
    fun executeGetUser(webView: WebView, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }
    /**
     * Execute a Puter.js chat function with streaming support
     */
    fun executeChatStream(webView: WebView, query: String, onChunkCallback: (String) -> Unit, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js file system write function
     */
    fun executeFsWrite(webView: WebView, path: String, data: String, options: String = "{}", callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js file system read function
     */
    fun executeFsRead(webView: WebView, path: String, options: String = "{}", callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js file system mkdir function
     */
    fun executeFsMkdir(webView: WebView, path: String, options: String = "{}", callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js file system readdir function
     */
    fun executeFsReaddir(webView: WebView, path: String, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js file system delete function
     */
    fun executeFsDelete(webView: WebView, path: String, options: String = "{}", callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js file system move function
     */
    fun executeFsMove(webView: WebView, source: String, destination: String, options: String = "{}", callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js file system copy function
     */
    fun executeFsCopy(webView: WebView, source: String, destination: String, options: String = "{}", callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js file system rename function
     */
    fun executeFsRename(webView: WebView, path: String, newName: String, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js file system stat function
     */
    fun executeFsStat(webView: WebView, path: String, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }

    /**
     * Execute a Puter.js file system space function
     */
    fun executeFsSpace(webView: WebView, callback: (String?) -> Unit) {
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
        
        webView.evaluateJavascript(jsCode, null)
    }
}