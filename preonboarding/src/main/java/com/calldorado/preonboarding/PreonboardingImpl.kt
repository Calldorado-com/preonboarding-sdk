package com.calldorado.preonboarding

import android.app.Activity
import com.calldorado.preonboarding.notification.NotificationManager
import com.calldorado.preonboarding.update.UpdateManager

class PreonboardingImpl {
    fun showNotification(){
        NotificationManager.getInstance().displayUpdateNotification()
    }

    fun updateImmediately(activity: Activity, statusOnUpdate : (Any) -> Unit) {
        UpdateManager.updateImmediately(UpdateManager.getInstance(activity), statusOnUpdate)
    }

    fun updateFlexibly() {
        TODO("Not yet implemented")
    }

    fun dismissNotification(){
        NotificationManager.getInstance().dismissNotification()
    }
}