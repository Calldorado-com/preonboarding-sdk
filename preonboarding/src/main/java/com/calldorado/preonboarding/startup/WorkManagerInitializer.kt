package com.calldorado.preonboarding.startup

import android.content.Context
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import timber.log.Timber
import java.lang.Exception

class WorkManagerInitializer : Initializer<WorkManager> {
    override fun create(context: Context): WorkManager {
        val configuration = Configuration.Builder().build()

        try {
            WorkManager.initialize(context, configuration)
        } catch (e :Exception){
            Timber.d("Work manager already initialized")
        }
        return WorkManager.getInstance(context)
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList() // No dependencies on other libraries.
    }
}