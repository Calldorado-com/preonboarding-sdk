package com.calldorado.preonboarding.notification

import android.content.Context
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.calldorado.preonboarding.Utils
import com.calldorado.preonboarding.update.UpdateManager

/*
    Is scheduled to execute after 15:00 hours by WorkManagerInitializer
 */
class NotificationWorker(val context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {
    companion object{
        const val NOTIFICATION_WORK_TAG = "notification_worker_tag"
    }

    override fun doWork(): Result {
        UpdateManager.isNewVersionAvailable(context) { newVersion ->
            if (newVersion && !Utils.isCalldoradoInstalled()) {
                NotificationManager.getInstance().displayUpdateNotification()
            } else {
                WorkManager.getInstance(context).cancelAllWorkByTag(NOTIFICATION_WORK_TAG)
            }
        }

       // Indicate whether the work finished successfully with the Result
       return Result.success()
   }
}
