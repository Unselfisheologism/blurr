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
import com.blurr.voice.activities.PuterWebViewActivity

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
        
        // Initialize PuterManager first
        puterManager = PuterManager.getInstance(this)
        puterManager.initialize()
        
        // Check if user is already authenticated
        if (puterManager.isUserSignedIn()) {
            // User is already signed in, navigate to appropriate screen based on onboarding status
            val onboardingManager = OnboardingManager(this)
            if (onboardingManager.isOnboardingCompleted()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, OnboardingPermissionsActivity::class.java))
            }
            finish()
            return
        }
        
        setContentView(R.layout.activity_onboarding)

        // Find the new Puter sign-in button
        puterSignInButton = findViewById(R.id.puterSignInButton)
        progressBar = findViewById(R.id.progressBar)
        loadingText = findViewById(R.id.loadingText)

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
        Log.d(TAG, "Sign in with Puter button clicked")
        // Instead of trying to authenticate directly, open the PuterWebViewActivity
        // where the user can interact with the web app UI
        val intent = Intent(this@LoginActivity, PuterWebViewActivity::class.java)
        startActivity(intent)
        finish() // Close LoginActivity and let the user authenticate in the web view
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
