package com.calldorado.preonboarding.notification

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.calldorado.preonboarding.R
import com.calldorado.preonboarding.UpdateActivity
import com.calldorado.preonboarding.Utils
import timber.log.Timber

class NotificationManager private constructor(val context: Context) {
    companion object {
        val TAG = NotificationManager::class.simpleName
        val CHANNEL_ID = 2860
        lateinit var context: Context
        private lateinit var notificationManager: NotificationManager
        private lateinit var androidNotificationManagerCompat: NotificationManagerCompat
        private lateinit var androidNotificationManager: android.app.NotificationManager

        fun initialize(ctx: Context): NotificationManager {
            context = ctx.applicationContext
            notificationManager = NotificationManager(ctx)
            return notificationManager
        }

        fun getInstance(): NotificationManager {
            if (!(Companion::notificationManager.isInitialized)) {
                throw Exception("Notification Manager not initialized. You might forgot to call initialize(ctx: Context) ")
            }
            Timber.d("getInstance:  $notificationManager]")
            androidNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            androidNotificationManagerCompat = NotificationManagerCompat.from(context)
            return notificationManager
        }

        fun displayUpdateNotification(notificationManager: NotificationManager) {
            createNotificationChannel()
            var builder = NotificationCompat.Builder(context, "$CHANNEL_ID")
                .setSmallIcon(R.drawable.baseline_update_24)
                .setContentTitle(context.getString(R.string.preonb_notification_header))
                .setContentText(context.getString(R.string.preonb_notification_body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent())
            androidNotificationManagerCompat.notify(CHANNEL_ID, builder.build())
        }

        private fun createNotificationChannel() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.preonb_notification_channel_name)
                val descriptionText = context.getString(R.string.preonb_notification_body)
                val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("$CHANNEL_ID", name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                androidNotificationManager.createNotificationChannel(channel)
            }
        }

        private fun contentIntent(): PendingIntent{
            val launcherClass = Utils.getMetadataLaunchClass(context)
            /*val intent = launcherClass?.let {
                Intent(
                    context,
                    Class.forName(it)
                    ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            } ?: context.packageManager.getLaunchIntentForPackage(context.packageName)*/


            val intent =
                Intent(
                    context,
                    UpdateActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

            Timber.d("Intent to launch ${intent.toString()}")
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent as Intent, 0)
            return pendingIntent
        }
    }
}
