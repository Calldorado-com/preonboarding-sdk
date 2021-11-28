package com.calldorado.preonboarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.calldorado.preonboarding.notification.NotificationManager
import com.calldorado.preonboarding.update.UpdateManager

class PreonboardingImpl {
    fun showNotification(){
        NotificationManager.getInstance().displayUpdateNotification()
    }

    fun updateImmediately(activity: Activity, statusOnUpdate : (Any) -> Unit) {
        UpdateManager.getInstance(activity).updateImmediately(statusOnUpdate)
    }

    fun updateFlexibly() {
        TODO("Not yet implemented")
    }

    fun dismissNotification(){
        NotificationManager.getInstance().dismissNotification()
    }

    fun testUpdateActivity(context: Context) {
        val intent =
            Intent(context, UpdateActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = NotificationManager.NOTIFICATION_ACTION
            }
        context.startActivity(intent)
    }
}