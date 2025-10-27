package com.blurr.voice.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blurr.voice.R
import com.blurr.voice.managers.PuterManager
import java.util.concurrent.CompletableFuture

class PuterTestActivity : AppCompatActivity() {
    private lateinit var puterManager: PuterManager
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puter_demo) // Using the existing layout

        puterManager = PuterManager.getInstance(this)
        resultTextView = findViewById(R.id.result_text) // Changed from resultText to result_text to match the layout
        val testButton = findViewById<Button>(R.id.chat_button) // Changed to use existing button in layout (chat_button)

        testButton.text = "Test KV Operations"
        testButton.setOnClickListener {
            testKVOperations()
        }

        // Initialize the Puter service
        puterManager.initialize()
    }

    private fun testKVOperations() {
        val results = StringBuilder()
        results.append("Testing KV Operations...\n")

        // Test KV Set
        puterManager.kvSet("test_key", "test_value").thenApply { success ->
            results.append("KV Set: $success\n")
            Log.d("PuterTest", "KV Set result: $success")

            // Test KV Get
            puterManager.kvGet("test_key").thenApply { value ->
                results.append("KV Get: $value\n")
                Log.d("PuterTest", "KV Get result: $value")

                // Test KV Increment
                puterManager.kvSet("counter", "10").thenApply { _ ->
                    puterManager.kvIncr("counter", 5).thenApply { newValue ->
                        results.append("KV Incr result: $newValue\n")
                        Log.d("PuterTest", "KV Incr result: $newValue")

                        // Test KV Decrement
                        puterManager.kvDecr("counter", 3).thenApply { newValue2 ->
                            results.append("KV Decr result: $newValue2\n")
                            Log.d("PuterTest", "KV Decr result: $newValue2")

                            // Test KV List
                            puterManager.kvList().thenApply { keys ->
                                results.append("KV List: $keys\n")
                                Log.d("PuterTest", "KV List result: $keys")

                                // Test KV Delete
                                puterManager.kvDel("test_key").thenApply { delSuccess ->
                                    results.append("KV Del: $delSuccess\n")
                                    Log.d("PuterTest", "KV Del result: $delSuccess")

                                    // Show all results
                                    runOnUiThread {
                                        resultTextView.text = results.toString()
                                        Toast.makeText(this, "KV Tests Completed!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.exceptionally { throwable ->
            Log.e("PuterTest", "Error during KV operations", throwable)
            runOnUiThread {
                resultTextView.text = "Error: ${throwable.message}"
                Toast.makeText(this, "Error during KV tests", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        puterManager.shutdown()
    }
}
