package com.blurr.voice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blurr.voice.utilities.OnboardingManager
import com.blurr.voice.utilities.UserProfileManager
import com.blurr.voice.managers.PuterManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var puterManager: PuterManager
    private lateinit var emailField: EditText
    private lateinit var emailSendLinkButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        emailField = findViewById(R.id.emailInput)
        emailSendLinkButton = findViewById(R.id.emailSendLinkButton)
        progressBar = findViewById(R.id.progressBar)
        loadingText = findViewById(R.id.loadingText)
        puterManager = PuterManager.getInstance(this)

        emailSendLinkButton.setOnClickListener {
            val email = emailField.text?.toString()?.trim()
            if (!email.isNullOrEmpty()) {
                sendSignInLink(email)
            } else {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendSignInLink(email: String) {
        progressBar.visibility = View.VISIBLE
        loadingText.visibility = View.VISIBLE
        emailSendLinkButton.isEnabled = false

        Log.d("LoginActivity", "Starting email sign-in with puter.js: $email")

        // Use puter.js email sign-in functionality
        puterManager.signIn()
        runOnUiThread {
            progressBar.visibility = View.GONE
            loadingText.visibility = View.GONE
            emailSendLinkButton.isEnabled = true

            // Check if sign in was successful
            if (puterManager.isUserSignedIn()) {
                Log.d("LoginActivity", "Puter.js email sign-in successful")
                startPostAuthFlow()
            } else {
                Log.w("LoginActivity", "Puter.js email sign-in failed")
                Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.let { handleEmailLinkIntent(it) }
    }

    override fun onStart() {
        super.onStart()
        handleEmailLinkIntent(intent)
    }

    private fun handleEmailLinkIntent(intent: Intent) {
        // Handle email link sign-in with puter.js
        Log.d("LoginActivity", "Handling email link intent with puter.js")
        val data = intent.data?.toString() ?: return
        Log.d("LoginActivity", "Email link data: $data")
        // For puter.js, email link handling would be different
    }

    private fun startPostAuthFlow() {
        lifecycleScope.launch {
            try {
                val name = puterManager.getUserName()
                val email = puterManager.getUserEmail()
                
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