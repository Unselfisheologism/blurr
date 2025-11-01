package com.blurr.voice.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.webkit.JavascriptInterface
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blurr.voice.R
import com.blurr.voice.PuterWebChromeClient
import com.blurr.voice.managers.PuterManager
import com.blurr.voice.utilities.OnboardingManager
import com.blurr.voice.MainActivity
import com.blurr.voice.OnboardingPermissionsActivity
import com.blurr.voice.services.PuterBackgroundService
import org.json.JSONObject

class PuterWebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var loginButton: Button
    private lateinit var doneButton: Button
    private lateinit var headerLayout: LinearLayout
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
        doneButton = findViewById(R.id.done_button)
        headerLayout = findViewById(R.id.header_layout)

        puterManager = PuterManager.getInstance(this)

        setupWebView()
        
        // Initially show the login button and hide the WebView
        webView.visibility = View.GONE
        loginButton.visibility = View.VISIBLE
        
        // Set click listener for the login button
        loginButton.setOnClickListener {
            onLoginClicked()
        }
        
        // Set click listener for the done button - always available
        doneButton.setOnClickListener {
            onDoneClicked()
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
            userAgentString = "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.0 Mobile Safari/537.36 PuterApp"
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
                Toast.makeText(this@PuterWebViewActivity, "Error loading web app: ${error?.description}", Toast.LENGTH_LONG).show()
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
        webView.loadUrl("https://puterwebp.vercel.app") // Updated to the correct deployed URL
    }

    // This function is called when the login button is clicked
    private fun onLoginClicked() {
        // Hide the login button and load the website
        loginButton.visibility = View.GONE
        loadPuterWebsite()
    }
    
    // This function is called when the done button is clicked - always available
    private fun onDoneClicked() {
        // Start the background service to maintain Puter.js communication
        val backgroundServiceIntent = Intent(this, PuterBackgroundService::class.java)
        startService(backgroundServiceIntent)
        
        // Minimize the activity to background
        moveTaskToBack(true)
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
                // Show success message
                Toast.makeText(this@PuterWebViewActivity, "Authentication successful!", Toast.LENGTH_SHORT).show()
                
                // Extract token from user JSON if available and store it
                try {
                    val userObject = JSONObject(userJson)
                    val token = userObject.optString("token", "")
                    if (token.isNotEmpty()) {
                        puterManager.initializeWithToken(token)
                        Log.d(TAG, "Token stored successfully")
                        
                        // Start the background service to maintain Puter.js communication
                        val backgroundServiceIntent = Intent(this@PuterWebViewActivity, PuterBackgroundService::class.java)
                        this@PuterWebViewActivity.startService(backgroundServiceIntent)
                    } else {
                        Log.w(TAG, "No token found in user JSON")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing user JSON", e)
                    // If we can't parse the JSON, continue anyway
                }
            }
        }

        @JavascriptInterface
        fun onPuterAuthError(error: String) {
            Log.e(TAG, "Authentication error: $error")
            // Handle authentication error
            runOnUiThread {
                Toast.makeText(this@PuterWebViewActivity, "Authentication failed: $error", Toast.LENGTH_LONG).show()
                // Show the login button again so user can try again
                loginButton.visibility = View.VISIBLE
                webView.visibility = View.GONE
            }
        }

        @JavascriptInterface
        fun onPuterActionSuccess(operation: String, result: String) {
            Log.d(TAG, "$operation successful: $result")
            // Handle successful Puter operation
            runOnUiThread {
                Toast.makeText(this@PuterWebViewActivity, "$operation successful!", Toast.LENGTH_SHORT).show()
            }
        }

        @JavascriptInterface
        fun onPuterActionError(operation: String, error: String) {
            Log.e(TAG, "$operation error: $error")
            // Handle Puter operation error
            runOnUiThread {
                Toast.makeText(this@PuterWebViewActivity, "Error in $operation: $error", Toast.LENGTH_LONG).show()
            }
        }

        @JavascriptInterface
        fun onPuterResponse(responseJson: String) {
            Log.d(TAG, "Generic response: $responseJson")
            // Handle generic response
            runOnUiThread {
                try {
                    val responseObject = JSONObject(responseJson)
                    val type = responseObject.optString("type", "unknown")
                    val data = responseObject.optString("data", "")
                    Log.d(TAG, "Processed $type response: $data")
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing response JSON", e)
                }
            }
        }
    }
}
