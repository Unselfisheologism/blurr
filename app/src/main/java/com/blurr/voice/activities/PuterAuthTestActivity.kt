package com.blurr.voice.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blurr.voice.R
import com.blurr.voice.utilities.PuterAuthIntegrationTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PuterAuthTestActivity : AppCompatActivity() {
    private lateinit var runTestsButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var testResultsTextView: TextView
    private lateinit var testAuthFlowButton: Button
    private val testScope = CoroutineScope(Dispatchers.Main)
    
    companion object {
        const val TAG = "PuterAuthTestActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puter_auth_test)
        
        // Initialize UI components
        runTestsButton = findViewById(R.id.run_all_tests_button)
        progressBar = findViewById(R.id.test_progress_bar)
        testResultsTextView = findViewById(R.id.test_results_text)
        testAuthFlowButton = findViewById(R.id.test_auth_flow_button)
        
        // Set click listeners
        runTestsButton.setOnClickListener {
            runAllTests()
        }
        
        testAuthFlowButton.setOnClickListener {
            testAuthenticationFlow()
        }
    }
    
    private fun runAllTests() {
        Log.d(TAG, "Running all authentication integration tests...")
        
        // Show progress
        progressBar.visibility = View.VISIBLE
        testResultsTextView.text = "Running tests..."
        runTestsButton.isEnabled = false
        testAuthFlowButton.isEnabled = false
        
        testScope.launch {
            try {
                val testSuite = PuterAuthIntegrationTest(this@PuterAuthTestActivity)
                val results = testSuite.runAllTests().get(30, java.util.concurrent.TimeUnit.SECONDS)
                
                // Update UI with results
                runOnUiThread {
                    displayTestResults(results)
                    progressBar.visibility = View.GONE
                    runTestsButton.isEnabled = true
                    testAuthFlowButton.isEnabled = true
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error running tests", e)
                runOnUiThread {
                    testResultsTextView.text = "Error running tests: ${e.message}"
                    progressBar.visibility = View.GONE
                    runTestsButton.isEnabled = true
                    testAuthFlowButton.isEnabled = true
                }
            }
        }
    }
    
    private fun testAuthenticationFlow() {
        Log.d(TAG, "Testing authentication flow...")
        
        // Show progress
        progressBar.visibility = View.VISIBLE
        testResultsTextView.text = "Testing authentication flow..."
        runTestsButton.isEnabled = false
        testAuthFlowButton.isEnabled = false
        
        testScope.launch {
            try {
                val testSuite = PuterAuthIntegrationTest(this@PuterAuthTestActivity)
                val success = testSuite.testAuthenticationFlowWithTimeout()
                
                // Update UI with results
                runOnUiThread {
                    if (success) {
                        testResultsTextView.text = "Authentication flow test PASSED"
                        Toast.makeText(this@PuterAuthTestActivity, "Authentication flow test PASSED", Toast.LENGTH_SHORT).show()
                    } else {
                        testResultsTextView.text = "Authentication flow test FAILED"
                        Toast.makeText(this@PuterAuthTestActivity, "Authentication flow test FAILED", Toast.LENGTH_LONG).show()
                    }
                    progressBar.visibility = View.GONE
                    runTestsButton.isEnabled = true
                    testAuthFlowButton.isEnabled = true
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error testing authentication flow", e)
                runOnUiThread {
                    testResultsTextView.text = "Error testing authentication flow: ${e.message}"
                    progressBar.visibility = View.GONE
                    runTestsButton.isEnabled = true
                    testAuthFlowButton.isEnabled = true
                }
            }
        }
    }
    
    private fun displayTestResults(results: Map<String, Boolean>) {
        val sb = StringBuilder()
        sb.append("Test Results:\n\n")
        
        var allPassed = true
        
        results.forEach { (testName, passed) ->
            val status = if (passed) "PASSED" else "FAILED"
            sb.append("$testName: $status\n")
            if (!passed) allPassed = false
        }
        
        sb.append("\nOverall: ${if (allPassed) "ALL TESTS PASSED" else "SOME TESTS FAILED"}")
        
        testResultsTextView.text = sb.toString()
        
        if (allPassed) {
            Toast.makeText(this, "All tests PASSED!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Some tests FAILED. Check results for details.", Toast.LENGTH_LONG).show()
        }
    }
}