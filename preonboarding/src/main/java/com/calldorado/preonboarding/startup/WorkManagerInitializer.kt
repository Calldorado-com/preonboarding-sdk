package com.calldorado.preonboarding.startup

import android.content.Context
import androidx.startup.Initializer
import androidx.work.*
import com.calldorado.preonboarding.Utils
import com.calldorado.preonboarding.notification.NotificationWorker
import com.calldorado.preonboarding.notification.NotificationWorker.Companion.NOTIFICATION_WORK_TAG
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.TimeUnit

class WorkManagerInitializer : Initializer<WorkManager> {
    override fun create(context: Context): WorkManager {
        val configuration = Configuration.Builder().build()

        try {
            WorkManager.initialize(context, configuration)
        } catch (e :Exception){
            Timber.d("Work manager already initialized")
        }

        val workManager = WorkManager.getInstance(context)

        /*val notificationWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<NotificationWorker>()
                .build()
        workManager.enqueue(notificationWorkRequest)*/

        if (!Utils.isCalldoradoInstalled()) {
            val delay = Utils.getMinutesUntilHour(context, 15)
            Timber.d("Delay until notification worker is run $delay")
            val periodicNotificationWorkRequest =
                PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                    .addTag(NOTIFICATION_WORK_TAG)
                    .setInitialDelay(delay, TimeUnit.MINUTES)
                    .build()
            workManager.enqueueUniquePeriodicWork(NOTIFICATION_WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, periodicNotificationWorkRequest)
        } else {
            Timber.d("Calldorado installed, cancelling notifications")
        }

        return workManager
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}