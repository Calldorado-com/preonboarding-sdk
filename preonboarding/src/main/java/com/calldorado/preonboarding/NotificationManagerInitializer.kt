package com.calldorado.preonboarding

import android.content.Context
import androidx.startup.Initializer
import androidx.work.Configuration

// Initializing  NotificationManager.
class NotificationManagerInitializer : Initializer<NotificationManager> {
    override fun create(context: Context): NotificationManager {
        val configuration = Configuration.Builder().build()
        return NotificationManager.initialize(context)
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}