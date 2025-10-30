package com.blurr.voice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.blurr.voice.utilities.OnboardingManager
import com.blurr.voice.utilities.UserProfileManager
import com.blurr.voice.managers.PuterManager

class LoginActivity : AppCompatActivity() {

    private lateinit var puterManager: PuterManager
    private lateinit var puterSignInButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView
    
    companion object {
        const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        var webview = findViewById<WebView>(R.id.webView)
        if (webView != null) {
            PuterManager.getInstance(this).setupWebView(webView)
        } else {
            Log.e(TAG, "WebView not found in layout")
        }

        // Find the new Puter sign-in button
        puterSignInButton = findViewById(R.id.puterSignInButton)
        progressBar = findViewById(R.id.progressBar)
        loadingText = findViewById(R.id.loadingText)
        puterManager = PuterManager.getInstance(this)

        // Initialize the PuterManager to bind the service
        puterManager.initialize()

        // Set click listener for the Puter sign-in button
        puterSignInButton.setOnClickListener {
            signInWithPuter()
        }
        
        // Handle authentication response from Custom Tabs
        handleAuthResponse(intent)
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAuthResponse(intent)
    }

    private fun signInWithPuter() {
        progressBar.visibility = View.VISIBLE
        loadingText.visibility = View.VISIBLE
        puterSignInButton.isEnabled = false
    
        // CORRECT: Use PuterManager's signIn method properly
        val signInFuture = PuterManager.getInstance(this).signIn()

        signInFuture.whenComplete { success, error -> 
            runOnUiThread {
                progressBar.visibility = View.GONE
                loadingText.visibility = View.GONE
                if(success == true) {
                    Toast.makeText(this, "Authentication successful!", Toast.LENGTH_SHORT).show()
                    startActivtiy(Intent(this, MainActivity::class.java))
                    finish()    
                } else {
                    puterSignInButton-isEnabled = trueToast.makeText (this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Sign in failed", error) 
                }
            }
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAuthResponse()
    }

    private fun handleAuthResponse(intent: Intent? = null) {
        val data = intent?.data ?: this.intent.data
        Log.d("LoginActivity", "Received intent: $data")
        // CRITICAL: Check if this is our authentication callback (must match puter_webview.html)
        if (data != null && data.toString().startsWith("blurr://auth")) {
            Log.d("LoginActivity", "Detected authentication callback with $data")
    
           // Extract token directly from query parameters (Puter.js uses query params, not fragment)
           val token = data.getQueryParameter("token")
           val error = data.getQueryParameter("error")
    
          if (!token.isNullOrEmpty()) {
              Log.d("LoginActivity", "Authentication successful, token received: ${token.take(5)}...")
              // Save token securely
              val editor = getSharedPreferences("auth", MODE_PRIVATE).edit()
              editor.putString("puter_token", token)
              editor.putBoolean("is_authenticated", true)
              editor.apply()
        
              // Proceed to main activity
              runOnUiThread {
                  progressBar.visibility = View.GONE
                  loadingText.visibility = View.GONE
                  Toast.makeText(this, "Successfully authenticated!", Toast.LENGTH_SHORT).show()
                  startActivity(Intent(this, MainActivity::class.java))
                  finish()
                }
            } else if (!error.isNullOrEmpty()) {
                Log.e("LoginActivity", "Authentication error: $error")
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    puterSignInButton.isEnabled = true
                    Toast.makeText(this, "Authentication failed: $error", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("LoginActivity", "Unknown authentication failure")
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    puterSignInButton.isEnabled = true
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startPostAuthFlow() {
        // For now, just navigate to main activity
        // In the future, we can get user info and save profile
        val onboardingManager = OnboardingManager(this)
        if (onboardingManager.isOnboardingCompleted()) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this@LoginActivity, OnboardingPermissionsActivity::class.java))
        }
        finish()
    }
}