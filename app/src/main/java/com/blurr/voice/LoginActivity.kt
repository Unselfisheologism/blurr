package com.blurr.voice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.blurr.voice.utilities.OnboardingManager
import com.blurr.voice.utilities.UserProfileManager
import com.blurr.voice.managers.PuterManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.future.await

class LoginActivity : AppCompatActivity() {

    private lateinit var puterManager: PuterManager
    private lateinit var puterSignInButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView
    
    companion object {
        const val REQUEST_CODE_PUTER_AUTH = 1001
        const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // Find the new Puter sign-in button
        puterSignInButton = findViewById(R.id.puterSignInButton)
        progressBar = findViewById(R.id.progressBar)
        loadingText = findViewById(R.id.loadingText)
        puterManager = PuterManager.getInstance(this)

        // Set click listener for the Puter sign-in button
        puterSignInButton.setOnClickListener {
            signInWithPuter()
        }
        
        // Handle authentication response from Custom Tabs
        handleAuthResponse(intent)
        
        // Register the auth URL callback with the PuterService after service is connected
        // We need to wait for the service to be connected before setting the callback
        setupAuthUrlCallbackWithRetry()
    }
    
    private fun setupAuthUrlCallbackWithRetry() {
        if (puterManager.isServiceBound) {
            setupAuthUrlCallback()
        } else {
            // Retry periodically until service is bound
            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            var retryCount = 0
            val maxRetries = 20 // 20 * 10ms = 200ms max wait time
            
            val retryRunnable = object : Runnable {
                override fun run() {
                    if (puterManager.isServiceBound) {
                        setupAuthUrlCallback()
                    } else if (retryCount < maxRetries) {
                        retryCount++
                        handler.postDelayed(this, 10)
                    } else {
                        Log.e(TAG, "PuterService not bound after maximum retries")
                        runOnUiThread {
                            puterSignInButton.isEnabled = true
                            Toast.makeText(this@LoginActivity, "Service not available. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            handler.post(retryRunnable)
        }
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
        
        Log.d(TAG, "Starting Puter.js sign-in via Custom Tabs")
        
        // Launch the authentication flow through the PuterService
        puterManager.signIn().thenApply { result ->
            Log.d(TAG, "Puter sign in process started: $result")
            // The actual authentication URL should be handled by the PuterService
            // which intercepts it and sends it to Android for Custom Tabs
        }.exceptionally { throwable ->
            Log.e(TAG, "Error starting sign in", throwable)
            runOnUiThread {
                progressBar.visibility = View.GONE
                loadingText.visibility = View.GONE
                puterSignInButton.isEnabled = true
                Toast.makeText(this, "Error starting authentication: ${throwable.message}", Toast.LENGTH_LONG).show()
            }
            null
        }
    }
    
    private fun setupAuthUrlCallback() {
        puterManager.getPuterService()?.setAuthUrlCallback { url ->
            handleAuthUrlRequest(url)
        }
    }
    
    // Method to handle the authentication URL request from the PuterService
    fun handleAuthUrlRequest(authUrl: String) {
        runOnUiThread {
            try {
                val customTabsIntent = CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(this, R.color.design_default_color_primary))
                    .build()
                
                Log.d(TAG, "Launching Custom Tabs for authentication: $authUrl")
                customTabsIntent.launchUrl(this, Uri.parse(authUrl))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to launch Custom Tabs", e)
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    puterSignInButton.isEnabled = true
                    Toast.makeText(this, "Browser not available. Please install Chrome.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    private fun handleAuthResponse(intent: Intent) {
        val data = intent.data
        Log.d(TAG, "Received intent: $data")
        
        // Check if this is our authentication callback
        if (data != null && data.toString().startsWith("blurr://puter-auth-callback")) {
            val token = data.getQueryParameter("token")
            val error = data.getQueryParameter("error")
            
            if (!token.isNullOrEmpty()) {
                Log.d(TAG, "Authentication successful, token received")
                // Save token securely
                val editor = getSharedPreferences("auth", MODE_PRIVATE).edit()
                editor.putString("puter_token", token)
                editor.apply()
                
                // Proceed to main activity
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    Toast.makeText(this, "Successfully authenticated!", Toast.LENGTH_SHORT).show()
                    startPostAuthFlow()
                }
            } else if (!error.isNullOrEmpty()) {
                Log.e(TAG, "Authentication error: $error")
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    puterSignInButton.isEnabled = true
                    Toast.makeText(this, "Authentication failed: $error", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e(TAG, "Unable to handle auth callback - no token or error provided")
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    puterSignInButton.isEnabled = true
                    Toast.makeText(this, "Authentication failed: No token received", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startPostAuthFlow() {
        lifecycleScope.launch {
            try {
                val nameFuture = puterManager.getUserName()
                val emailFuture = puterManager.getUserEmail()
                
                val name = nameFuture.await()
                val email = emailFuture.await()
                
                val profileManager = UserProfileManager(this@LoginActivity)
                profileManager.saveProfile(name, email)

                val onboardingManager = OnboardingManager(this@LoginActivity)
                if (onboardingManager.isOnboardingCompleted()) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@LoginActivity, OnboardingPermissionsActivity::class.java))
                }
                finish()
            } catch (e: Exception) {
                Log.e(TAG, "Error getting user info", e)
                Toast.makeText(this@LoginActivity, "Error getting user info", Toast.LENGTH_SHORT).show()
            }
        }
    }
}