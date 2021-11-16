package com.calldorado.preonboarding

import android.app.Activity
import android.content.Context

object PreonboardingApi {
    const val UPDATE_REQUEST_CODE = 3520

    fun showNotification() {
        PreonboardingImpl().showNotification()
    }

    fun updateImmediately(activity: Activity, statusOnUpdate : (Any) -> Unit) {
        PreonboardingImpl().updateImmediately(activity, statusOnUpdate)
    }

    fun updateFlexibly() {
        PreonboardingImpl().updateFlexibly()
    }

    fun dismissNotification() {
        PreonboardingImpl().dismissNotification()
    }

    fun testUpdateActivity(context: Context) {
        PreonboardingImpl().testUpdateActivity(context)
    }
}