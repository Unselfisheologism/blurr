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
      
        // Set up callback to intercept auth URL from WebView  
        puterManager.getPuterService()?.setAuthUrlCallback { url ->  
            Log.d(TAG, "Auth URL intercepted from WebView: $url")  
            runOnUiThread {  
                try {  
                    val customTabsIntent = CustomTabsIntent.Builder()  
                        .setToolbarColor(ContextCompat.getColor(this, R.color.primary))  
                        .build()  
                    customTabsIntent.launchUrl(this, Uri.parse(url))  
                } catch (e: Exception) {  
                    Log.e(TAG, "Failed to launch Custom Tabs", e)  
                    Toast.makeText(this, "Browser not available", Toast.LENGTH_LONG).show()  
                }  
            }  
        }  
      
        // Trigger puter.auth.signIn() in the WebView  
        puterManager.signIn()  
    }
    
    private fun handleAuthResponse(intent: Intent? = null) {
        val data = intent?.data ?: this.intent.data
        Log.d("LoginActivity", "Received intent: $data")
        
        // Check if this is our authentication callback
        if (data != null && data.toString().startsWith("myblurr://auth")) {
            // Extract the token from the fragment (for implicit flow) or query parameters
            val fragment = data.fragment
            var token: String? = null
            
            // For implicit flow, the token is in the fragment
            if (!fragment.isNullOrEmpty()) {
                val params = fragment.split("&")
                for (param in params) {
                    if (param.startsWith("access_token=")) {
                        token = param.substring("access_token=".length)
                        break
                    }
                }
            }
            
            // If not in fragment, check query parameters (for authorization code flow)
            if (token == null) {
                token = data.getQueryParameter("access_token")
            }
            
            val error = data.getQueryParameter("error")
            val errorDescription = data.getQueryParameter("error_description")
            
            if (!token.isNullOrEmpty()) {
                Log.d("LoginActivity", "Authentication successful, token received")
                // Save token securely
                val editor = getSharedPreferences("auth", MODE_PRIVATE).edit()
                editor.putString("puter_token", token)
                editor.apply()
                
                // Initialize Puter with the token
                puterManager.initializeWithToken(token)
                
                // Proceed to main activity
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    Toast.makeText(this, "Successfully authenticated!", Toast.LENGTH_SHORT).show()
                    startPostAuthFlow()
                }
            } else if (!error.isNullOrEmpty()) {
                Log.e("LoginActivity", "Authentication error: $error - $errorDescription")
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    puterSignInButton.isEnabled = true
                    Toast.makeText(this, "Authentication failed: $error - $errorDescription", Toast.LENGTH_SHORT).show()
                }
            } else {
                // No token and no error - might be a cancelled auth
                Log.d("LoginActivity", "No token received, authentication may have been cancelled")
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    puterSignInButton.isEnabled = true
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