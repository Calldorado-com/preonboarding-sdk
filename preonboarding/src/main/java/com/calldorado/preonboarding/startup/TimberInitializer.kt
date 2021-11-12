package com.calldorado.preonboarding.startup

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import timber.log.Timber
import timber.log.Timber.DebugTree

// Initializing Timber logs.
class TimberInitializer : Initializer<List<Timber.Tree>> {
    override fun create(context: Context): List<Timber.Tree> {
        Timber.plant(DebugTree())
        Timber.d("init'ed timber trees")
        return Timber.forest()
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}