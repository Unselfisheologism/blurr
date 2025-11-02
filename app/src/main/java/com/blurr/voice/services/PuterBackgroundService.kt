package com.blurr.voice.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import androidx.core.app.NotificationCompat
import com.blurr.voice.PuterWebChromeClient

class PuterBackgroundService : Service() {
    private var webView: WebView? = null
    private val binder = PuterBinder()
    private val callbacks = mutableMapOf<String, (String?) -> Unit>()
    private val TAG = "PuterBackgroundService"

    companion object {
        const val CHANNEL_ID = "PuterBackgroundServiceChannel"
        const val NOTIFICATION_ID = 1
    }

    inner class PuterBinder : Binder() {
        fun getService(): PuterBackgroundService = this@PuterBackgroundService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        initializeWebView()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Puter Background Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Maintains connection to Puter.js SDK"
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Puter Connection Active")
            .setContentText("Maintaining connection to Puter.js SDK")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }

    private fun initializeWebView() {
        try {
            webView = WebView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    1, // Very small size to minimize resource usage
                    1
                )
                
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    setSupportMultipleWindows(true)
                    javaScriptCanOpenWindowsAutomatically = true
                    allowFileAccess = true
                    allowContentAccess = true
                    databaseEnabled = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    userAgentString = "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.0 Mobile Safari/537.36 PuterApp"
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d(TAG, "Background WebView page finished loading: $url")
                        // Inject Android interface after page loads
                        view?.addJavascriptInterface(AndroidInterface(), "Android")
                    }

                    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                        super.onReceivedError(view, request, error)
                        Log.e(TAG, "WebView error: ${error?.description}")
                    }
                }

                webChromeClient = PuterWebChromeClient()
            }

            // Load the Puter website
            webView?.loadUrl("file:///android_asset/puterwebp.html")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing background WebView", e)
        }
    }

    fun evaluateJavascript(jsCode: String, resultCallback: ValueCallback<String>?) {
        webView?.post {
            webView?.evaluateJavascript(jsCode, resultCallback)
        }
    }

    fun loadUrl(url: String) {
        webView?.post {
            webView?.loadUrl(url)
        }
    }

    inner class AndroidInterface {
        @JavascriptInterface
        fun onAIResponse(response: String, callbackId: String) {
            Log.d(TAG, "Received response from Puter.js: $response, callbackId: $callbackId")
            callbacks.remove(callbackId)?.invoke(response)
        }

        @JavascriptInterface
        fun onAIError(error: String, callbackId: String) {
            Log.e(TAG, "Received error from Puter.js: $error, callbackId: $callbackId")
            callbacks.remove(callbackId)?.invoke(null)
        }
        
        @JavascriptInterface
        fun onAuthSuccess(response: String) {
            Log.d(TAG, "Received auth success in background service: $response")
            // Handle authentication success in background
        }
        
        @JavascriptInterface
        fun onAuthError(error: String) {
            Log.e(TAG, "Received auth error in background service: $error")
            // Handle authentication error in background
        }
        
        @JavascriptInterface
        fun handleAuthResponse(token: String) {
            Log.d(TAG, "Received auth response from JavaScript in background: $token")
            // Handle authentication response in background
        
        @JavascriptInterface
        fun updateAuthState(signedIn: Boolean) {
            Log.d(TAG, "Updating auth state from web interface in background: $signedIn")
            // This method will be called to update the native service's authentication state
            // based on changes in the web authentication status
        }
        
        @JavascriptInterface
        fun checkAuthStatus(): String {
            Log.d(TAG, "Checking auth status from web interface in background")
            // For background service, we can return a simple check
            return "false" // Background service doesn't typically handle auth directly
        }
        }
    }

    override fun onDestroy() {
        webView?.destroy()
        webView = null
        super.onDestroy()
    }
}