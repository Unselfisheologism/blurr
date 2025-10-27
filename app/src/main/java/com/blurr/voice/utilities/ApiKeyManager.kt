package com.blurr.voice.utilities

import android.content.Context
import com.blurr.voice.BuildConfig

/**
 * A thread-safe, singleton object to manage and rotate a list of API keys.
 * This ensures that every part of the app gets the next key in the sequence.
 */
class ApiKeyManager private constructor(context: Context) {

    val context: Context = context.applicationContext

    companion object {
        @Volatile
        private var INSTANCE: ApiKeyManager? = null

        fun getInstance(context: Context): ApiKeyManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiKeyManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}