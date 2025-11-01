package com.blurr.voice.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.webkit.JavascriptInterface
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.blurr.voice.R
import com.blurr.voice.PuterWebChromeClient
import com.blurr.voice.managers.PuterManager

class PuterWebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var loginButton: Button
    private lateinit var puterManager: PuterManager

    companion object {
        const val TAG = "PuterWebViewActivity"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puter_webview)

        webView = findViewById(R.id.puterWebView)
        progressBar = findViewById(R.id.progressBar)
        loginButton = findViewById(R.id.login_button)

        puterManager = PuterManager.getInstance(this)

        setupWebView()
        
        // Initially show the login button and hide the WebView
        webView.visibility = View.GONE
        loginButton.visibility = View.VISIBLE
        
        // Set click listener for the login button
        loginButton.setOnClickListener {
            onLoginClicked()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            setSupportMultipleWindows(true)
            javaScriptCanOpenWindowsAutomatically = true
            allowFileAccess = true
            allowContentAccess = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            userAgentString = "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.0.0 Mobile Safari/537.36 PuterApp"
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                Log.d(TAG, "WebView started loading: $url")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                Log.d(TAG, "WebView finished loading: $url")
                
                // Hide the login button after page loads since the website now has its own UI
                loginButton.visibility = View.GONE
                
                // The website is now loaded and the user can interact with it
                // including any authentication buttons on the website itself
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = View.GONE
                Log.e(TAG, "WebView error: ${error?.description}")
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                Log.d(TAG, "WebView loading URL: $url")
                return false
            }
        }

        webView.webChromeClient = PuterWebChromeClient()
        
        // Add JavaScript interface for communication with the web app
        webView.addJavascriptInterface(AndroidInterface(this), "Android")
    }

    private fun loadPuterWebsite() {
        webView.visibility = View.VISIBLE
        webView.loadUrl("https://puterwebp.vercel.app") // Replace with actual deployed URL when available
    }

    // This function is called when the login button is clicked
    private fun onLoginClicked() {
        // Hide the login button and load the website
        loginButton.visibility = View.GONE
        loadPuterWebsite()
    }

    override fun onBackPressed() {
        if (webView.visibility == View.VISIBLE && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
    
    // Android interface for communication with the web app
    inner class AndroidInterface(private val context: Context) {
        @JavascriptInterface
        fun onPuterAuthSuccess(userJson: String) {
            Log.d(TAG, "Authentication successful: $userJson")
            // Handle successful authentication
            runOnUiThread {
                // Update UI or store user information
                // You can broadcast the success to other parts of the app
            }
        }

        @JavascriptInterface
        fun onPuterAuthError(error: String) {
            Log.e(TAG, "Authentication error: $error")
            // Handle authentication error
            runOnUiThread {
                // Update UI to show error
            }
        }

        @JavascriptInterface
        fun onPuterActionSuccess(operation: String, result: String) {
            Log.d(TAG, "$operation successful: $result")
            // Handle successful Puter operation
            runOnUiThread {
                // Update UI based on the operation result
            }
        }

        @JavascriptInterface
        fun onPuterActionError(operation: String, error: String) {
            Log.e(TAG, "$operation error: $error")
            // Handle Puter operation error
            runOnUiThread {
                // Update UI to show error
            }
        }

        @JavascriptInterface
        fun onPuterResponse(responseJson: String) {
            Log.d(TAG, "Generic response: $responseJson")
            // Handle generic response
            runOnUiThread {
                // Process the response as needed
            }
        }
    }
}
