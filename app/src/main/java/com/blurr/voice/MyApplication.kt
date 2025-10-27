package com.blurr.voice

import android.app.Application
import android.content.Context
import com.blurr.voice.utilities.Logger
import com.blurr.voice.intents.IntentRegistry
import com.blurr.voice.intents.impl.DialIntent
import com.blurr.voice.intents.impl.EmailComposeIntent
import com.blurr.voice.intents.impl.ShareTextIntent
import com.blurr.voice.intents.impl.ViewUrlIntent
import com.blurr.voice.managers.PuterManager

class MyApplication : Application() {

    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        // Initialize puter.js for remote config functionality
        PuterManager.getInstance(this)

        IntentRegistry.register(DialIntent())
        IntentRegistry.register(ViewUrlIntent())
        IntentRegistry.register(ShareTextIntent())
        IntentRegistry.register(EmailComposeIntent())
        IntentRegistry.init(this)
    }
}