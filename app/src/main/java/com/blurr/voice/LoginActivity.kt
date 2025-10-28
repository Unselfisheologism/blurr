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
    }

    private fun signInWithPuter() {
        progressBar.visibility = View.VISIBLE
        loadingText.visibility = View.VISIBLE
        puterSignInButton.isEnabled = false

        Log.d("LoginActivity", "Starting Puter.js sign-in")

        // Use puter.js authentication functionality
        val signInFuture = puterManager.signIn()
        signInFuture.whenComplete { success, throwable ->
            runOnUiThread {
                progressBar.visibility = View.GONE
                loadingText.visibility = View.GONE
                puterSignInButton.isEnabled = true

                if (success) {
                    Log.d("LoginActivity", "Puter.js sign-in successful")
                    startPostAuthFlow()
                } else {
                    Log.w("LoginActivity", "Puter.js sign-in failed", throwable)
                    Toast.makeText(this, "Authentication Failed: ${throwable?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Handle any authentication redirect if needed
    }

    override fun onStart() {
        super.onStart()
        // Check if user is already signed in
        puterManager.isSignedIn().whenComplete { signedIn, throwable ->
            if (signedIn) {
                runOnUiThread {
                    startPostAuthFlow()
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