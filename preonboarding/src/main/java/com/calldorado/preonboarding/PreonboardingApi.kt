package com.calldorado.preonboarding

import android.app.Activity

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
}