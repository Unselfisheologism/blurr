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

class PuterFullTestActivity : AppCompatActivity() {
    private lateinit var puterManager: PuterManager
    private lateinit var resultTextView: TextView
    private lateinit var testButton: Button

    companion object {
        const val TAG = "PuterFullTestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puter_demo)

        puterManager = PuterManager.getInstance(this)
        resultTextView = findViewById(R.id.result_text)
        testButton = findViewById(R.id.chat_button)
        val signInButton = findViewById<Button>(R.id.sign_in_button)
        val kvGetButton = findViewById<Button>(R.id.kv_get_button)
        val kvSetButton = findViewById<Button>(R.id.kv_set_button)

        testButton.text = "Test All Puter.js Features"
        testButton.setOnClickListener {
            testAllFeatures()
        }

        signInButton.setOnClickListener {
            testAuth()
        }

        kvGetButton.setOnClickListener {
            testKV()
        }

        kvSetButton.setOnClickListener {
            testFs()
        }

        puterManager.initialize()
    }

    private fun testAllFeatures() {
        val results = StringBuilder()
        results.append("Testing All Puter.js Features...\n\n")

        // Test authentication first
        puterManager.isSignedIn().thenApply { signedIn ->
            results.append("Is signed in: $signedIn\n")
            Log.d(TAG, "Is signed in: $signedIn")

            if (!signedIn) {
                results.append("Not signed in, please sign in first.\n")
                runOnUiThread {
                    resultTextView.text = results.toString()
                    Toast.makeText(this, "Please sign in first", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Test KV operations
                testKVOperations(results)
            }
        }.exceptionally { throwable ->
            Log.e(TAG, "Error checking auth status", throwable)
            runOnUiThread {
                resultTextView.text = "Error: ${throwable.message}"
                Toast.makeText(this, "Auth check error", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    private fun testKVOperations(results: StringBuilder) {
        // Test KV Set
        puterManager.kvSet("test_key", "test_value").thenApply { success ->
            results.append("KV Set: $success\n")
            Log.d(TAG, "KV Set result: $success")

            // Test KV Get
            puterManager.kvGet("test_key").thenApply { value ->
                results.append("KV Get: $value\n")
                Log.d(TAG, "KV Get result: $value")

                // Test KV Increment
                puterManager.kvSet("counter", "10").thenApply { _ ->
                    puterManager.kvIncr("counter", 5).thenApply { newValue ->
                        results.append("KV Incr result: $newValue\n")
                        Log.d(TAG, "KV Incr result: $newValue")

                        // Test KV Decrement
                        puterManager.kvDecr("counter", 3).thenApply { newValue2 ->
                            results.append("KV Decr result: $newValue2\n")
                            Log.d(TAG, "KV Decr result: $newValue2")

                            // Test KV List
                            puterManager.kvList().thenApply { keys ->
                                results.append("KV List: $keys\n")
                                Log.d(TAG, "KV List result: $keys")

                                // Test KV Delete
                                puterManager.kvDel("test_key").thenApply { delSuccess ->
                                    results.append("KV Del: $delSuccess\n")
                                    Log.d(TAG, "KV Del result: $delSuccess")

                                    // Now test AI features
                                    testAIFeatures(results)
                                }
                            }
                        }
                    }
                }
            }
        }.exceptionally { throwable ->
            Log.e(TAG, "Error during KV operations", throwable)
            runOnUiThread {
                resultTextView.text = "Error: ${throwable.message}"
                Toast.makeText(this, "KV test error", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    private fun testAIFeatures(results: StringBuilder) {
        // Test Chat
        puterManager.chat("What is the capital of France?").thenApply { response ->
            results.append("Chat response: $response\n")
            Log.d(TAG, "Chat response: $response")

            // Test streaming chat
            puterManager.chatStream("Count from 1 to 5", onChunkCallback = { chunk ->
                Log.d(TAG, "Stream chunk: $chunk")
            }).thenApply { response ->
                results.append("Streaming chat response: $response\n")
                Log.d(TAG, "Streaming chat response: $response")

                // Test Txt2Img (with a simple prompt to avoid costs in testing)
                puterManager.txt2img("A simple geometric shape").thenApply { imgResponse ->
                    results.append("Txt2Img response type: ${imgResponse.javaClass.simpleName}\n")
                    Log.d(TAG, "Txt2Img response type: ${imgResponse.javaClass.simpleName}")

                    // Test Img2Txt
                    // Note: We can't test this properly without an image to process
                    results.append("Img2Txt: Skipped (requires image input)\n")

                    // Test Txt2Speech
                    puterManager.txt2speech("Hello from Puter.js").thenApply { speechResponse ->
                        results.append("Txt2Speech response type: ${speechResponse.javaClass.simpleName}\n")
                        Log.d(TAG, "Txt2Speech response type: ${speechResponse.javaClass.simpleName}")

                        // Now test file system features
                        testFsFeatures(results)
                    }
                }
            }
        }.exceptionally { throwable ->
            Log.e(TAG, "Error during AI operations", throwable)
            runOnUiThread {
                resultTextView.text = "Error: ${throwable.message}"
                Toast.makeText(this, "AI test error", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    private fun testFsFeatures(results: StringBuilder) {
        // Test writing a file
        puterManager.fsWrite("test_file.txt", "Hello from Android app!").thenApply { response ->
            results.append("FS Write response: $response\n")
            Log.d(TAG, "FS Write response: $response")

            // Test reading the file
            puterManager.fsRead("test_file.txt").thenApply { readResponse ->
                results.append("FS Read response: $readResponse\n")
                Log.d(TAG, "FS Read response: $readResponse")

                // Test creating a directory
                puterManager.fsMkdir("test_directory").thenApply { mkdirResponse ->
                    results.append("FS Mkdir response: $mkdirResponse\n")
                    Log.d(TAG, "FS Mkdir response: $mkdirResponse")

                    // Test listing directory contents
                    puterManager.fsReaddir("./").thenApply { readdirResponse ->
                        results.append("FS Readdir response (first 200 chars): ${readdirResponse.take(200)}...\n")
                        Log.d(TAG, "FS Readdir response length: ${readdirResponse.length}")

                        // Test getting file stats
                        puterManager.fsStat("test_file.txt").thenApply { statResponse ->
                            results.append("FS Stat response (first 200 chars): ${statResponse.take(200)}...\n")
                            Log.d(TAG, "FS Stat response: $statResponse")

                            // Test getting space usage
                            puterManager.fsSpace().thenApply { spaceResponse ->
                                results.append("FS Space response (first 200 chars): ${spaceResponse.take(200)}...\n")
                                Log.d(TAG, "FS Space response: $spaceResponse")

                                // Test renaming the file
                                puterManager.fsRename("test_file.txt", "renamed_test_file.txt").thenApply { renameResponse ->
                                    results.append("FS Rename response: $renameResponse\n")
                                    Log.d(TAG, "FS Rename response: $renameResponse")

                                    // Test copying the file
                                    puterManager.fsCopy("renamed_test_file.txt", "copied_test_file.txt").thenApply { copyResponse ->
                                        results.append("FS Copy response: $copyResponse\n")
                                        Log.d(TAG, "FS Copy response: $copyResponse")

                                        // Test moving the file
                                        puterManager.fsMove("copied_test_file.txt", "moved_test_file.txt").thenApply { moveResponse ->
                                            results.append("FS Move response: $moveResponse\n")
                                            Log.d(TAG, "FS Move response: $moveResponse")

                                            // Test deleting the files and directory
                                            puterManager.fsDelete("renamed_test_file.txt").thenApply { del1Response ->
                                                results.append("FS Delete 1 response: $del1Response\n")
                                                Log.d(TAG, "FS Delete 1 response: $del1Response")

                                                puterManager.fsDelete("moved_test_file.txt").thenApply { del2Response ->
                                                    results.append("FS Delete 2 response: $del2Response\n")
                                                    Log.d(TAG, "FS Delete 2 response: $del2Response")

                                                    puterManager.fsDelete("test_directory").thenApply { delDirResponse ->
                                                        results.append("FS Delete directory response: $delDirResponse\n")
                                                        Log.d(TAG, "FS Delete directory response: $delDirResponse")

                                                        // All tests completed
                                                        runOnUiThread {
                                                            resultTextView.text = results.toString()
                                                            Toast.makeText(this, "All tests completed!", Toast.LENGTH_LONG).show()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.exceptionally { throwable ->
            Log.e(TAG, "Error during FS operations", throwable)
            runOnUiThread {
                resultTextView.text = "Error: ${throwable.message}"
                Toast.makeText(this, "FS test error", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    private fun testAuth() {
        resultTextView.text = "Testing authentication..."

        puterManager.isSignedIn().thenApply { signedIn ->
            runOnUiThread {
                if (signedIn) {
                    resultTextView.text = "Already signed in!\nTesting user info retrieval..."
                    puterManager.getUser().thenApply { userJson ->
                        runOnUiThread {
                            resultTextView.text = "User info: $userJson"
                        }
                    }.exceptionally { throwable ->
                        runOnUiThread {
                            resultTextView.text = "Error getting user info: ${throwable.message}"
                        }
                        null
                    }
                } else {
                    resultTextView.text = "Signing in..."
                    puterManager.signIn().thenApply { success ->
                        runOnUiThread {
                            if (success) {
                                resultTextView.text = "Sign in successful!"
                                Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                resultTextView.text = "Sign in failed"
                                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.exceptionally { throwable ->
                        runOnUiThread {
                            resultTextView.text = "Sign in error: ${throwable.message}"
                            Toast.makeText(this, "Sign in error", Toast.LENGTH_SHORT).show()
                        }
                        null
                    }
                }
            }
        }.exceptionally { throwable ->
            runOnUiThread {
                resultTextView.text = "Auth check error: ${throwable.message}"
                Toast.makeText(this, "Auth error", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    private fun testKV() {
        resultTextView.text = "Testing KV operations..."

        // Test setting a value
        puterManager.kvSet("demo_key", "demo_value").thenApply { success ->
            if (success) {
                // Test getting the value back
                puterManager.kvGet("demo_key").thenApply { value ->
                    runOnUiThread {
                        resultTextView.text = "KV Test - Set: true, Get: $value"
                        Toast.makeText(this, "KV test completed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                runOnUiThread {
                    resultTextView.text = "KV Set failed"
                }
            }
        }.exceptionally { throwable ->
            runOnUiThread {
                resultTextView.text = "KV test error: ${throwable.message}"
                Toast.makeText(this, "KV test error", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    private fun testFs() {
        resultTextView.text = "Testing File System operations..."

        // Test writing a file
        puterManager.fsWrite("demo_file.txt", "Hello from Android!").thenApply { response ->
            Log.d(TAG, "FS Write response: $response")
            
            // Test reading the file
            puterManager.fsRead("demo_file.txt").thenApply { readResponse ->
                runOnUiThread {
                    resultTextView.text = "FS Test - Write: Success, Read: $readResponse"
                    Toast.makeText(this, "FS test completed", Toast.LENGTH_SHORT).show()
                }
            }
        }.exceptionally { throwable ->
            runOnUiThread {
                resultTextView.text = "FS test error: ${throwable.message}"
                Toast.makeText(this, "FS test error", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        puterManager.shutdown()
    }
}