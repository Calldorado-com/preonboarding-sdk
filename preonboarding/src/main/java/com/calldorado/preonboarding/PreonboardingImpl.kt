package com.calldorado.preonboarding

class PreonboardingImpl {
    fun showNotification(){
        NotificationManager.displayUpdateNotification(NotificationManager.getInstance())
    }
}