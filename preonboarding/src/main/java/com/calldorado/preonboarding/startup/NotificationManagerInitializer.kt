package com.calldorado.preonboarding.startup

import android.content.Context
import androidx.startup.Initializer
import androidx.work.Configuration
import com.calldorado.preonboarding.notification.NotificationManager
import timber.log.Timber

// Initializing  NotificationManager.
class NotificationManagerInitializer : Initializer<NotificationManager> {
    override fun create(context: Context): NotificationManager {
        val configuration = Configuration.Builder().build()
        Timber.d("initing notification manager")
        return NotificationManager.initialize(context)
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(WorkManagerInitializer::class.java)
    }

}
