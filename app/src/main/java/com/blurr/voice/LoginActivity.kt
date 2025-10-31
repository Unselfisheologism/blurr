package com.blurr.voice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // No longer needed since we're using popup WebView for auth
    }

    private fun signInWithPuter() {
        progressBar.visibility = View.VISIBLE
        loadingText.visibility = View.VISIBLE
        puterSignInButton.isEnabled = false
    
        // Use PuterManager's signIn method which will handle popup WebView
        val signInFuture = PuterManager.getInstance(this).signIn()

        signInFuture.whenComplete { success, error ->
            runOnUiThread {
                progressBar.visibility = View.GONE
                loadingText.visibility = View.GONE
                if (success == true) {
                    Toast.makeText(this, "Authentication successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()    
                } else {
                    puterSignInButton.isEnabled = true
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Sign in failed", error) 
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