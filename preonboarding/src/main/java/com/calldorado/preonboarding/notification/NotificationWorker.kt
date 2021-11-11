package com.calldorado.preonboarding.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.calldorado.preonboarding.Utils
import com.calldorado.preonboarding.update.UpdateManager
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

/*
    Is scheduled to execute after 15:00 hours by WorkManagerInitializer
 */
class NotificationWorker(val context: Context, workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {
    companion object{
        const val NOTIFICATION_WORK_TAG = "notification_worker_tag"
    }

    override suspend fun doWork(): Result {
        var result = Result.success()
        Timber.d( "doWork()!")
        runBlocking {
            val job = async {
                try {
                    UpdateManager.isNewVersionAvailable(context) { newVersion ->
                        Timber.d("Done with async")
                        if (newVersion && !Utils.isCalldoradoInstalled()) {
                            NotificationManager.getInstance().displayUpdateNotification()
                        } else {
                            WorkManager.getInstance(context).cancelAllWorkByTag(NOTIFICATION_WORK_TAG)
                        }
                    }
                } catch (e: Exception){
                    Timber.e(e)
                    e.printStackTrace()
                }
            }
            job.await()
        }
        return result
   }
}
