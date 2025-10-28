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
import android.content.SharedPreferences

class LoginActivity : AppCompatActivity() {

    private lateinit var puterManager: PuterManager
    private lateinit var puterSignInButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView
    
    companion object {
        const val REQUEST_CODE_PUTER_AUTH = 1001
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
        
        // THIS IS THE CRITICAL CHANGE:
        // Don't try to trigger authentication through WebView
        // Instead, directly launch Custom Tabs with the authentication URL
        val authUrl = "https://puter.com/action/sign-in?msg_id=1"
        
        try {
            val customTabsIntent = CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))
                .build()
            
            Log.d("LoginActivity", "Launching Custom Tabs for authentication")
            customTabsIntent.launchUrl(this, Uri.parse(authUrl))
        } catch (e: Exception) {
            Log.e("LoginActivity", "Failed to launch Custom Tabs", e)
            runOnUiThread {
                progressBar.visibility = View.GONE
                loadingText.visibility = View.GONE
                puterSignInButton.isEnabled = true
                Toast.makeText(this, "Browser not available. Please install Chrome.", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun handleAuthResponse(intent: Intent) {
        val data = intent.data
        Log.d("LoginActivity", "Received intent: $data")
        
        // Check if this is our authentication callback
        if (data != null && data.toString().startsWith("blurr://puter-auth-callback")) {
            val token = data.getQueryParameter("token")
            val error = data.getQueryParameter("error")
            
            if (!token.isNullOrEmpty()) {
                Log.d("LoginActivity", "Authentication successful, token received")
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
                Log.e("LoginActivity", "Authentication error: $error")
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    puterSignInButton.isEnabled = true
                    Toast.makeText(this, "Authentication failed: $error", Toast.LENGTH_SHORT).show()
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
                Log.e("LoginActivity", "Error getting user info", e)
                Toast.makeText(this@LoginActivity, "Error getting user info", Toast.LENGTH_SHORT).show()
            }
        }
    }
}