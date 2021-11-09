package com.calldorado.preonboarding

import android.app.Activity
import com.calldorado.preonboarding.notification.NotificationManager
import com.calldorado.preonboarding.update.UpdateManager

class PreonboardingImpl {
    fun showNotification(){
        NotificationManager.displayUpdateNotification(NotificationManager.getInstance())
    }

    fun updateImmediately(activity: Activity) {
        UpdateManager.updateImmediately(UpdateManager.getInstance(activity))
    }

    fun updateFlexibly() {
        TODO("Not yet implemented")
    }

}