package com.calldorado.preonboarding

import android.app.Activity

object PreonboardingApi {
    val UPDATE_REQUEST_CODE = 3520

    fun showNotification() {
        PreonboardingImpl().showNotification()
    }

    fun updateImmediately(activity: Activity) {
        PreonboardingImpl().updateImmediately(activity)
    }

    fun updateFlexibly() {
        PreonboardingImpl().updateFlexibly()
    }
}