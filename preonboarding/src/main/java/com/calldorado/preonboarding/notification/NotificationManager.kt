package com.calldorado.preonboarding.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.calldorado.preonboarding.BuildConfig
import com.calldorado.preonboarding.R
import com.calldorado.preonboarding.UpdateActivity
import com.calldorado.preonboarding.UpdateActivity.Companion.NOTIFICATION_REQ_CODE
import com.calldorado.preonboarding.Utils
import timber.log.Timber


class NotificationManager private constructor(val context: Context) {

    companion object {
        val TAG = NotificationManager::class.simpleName
        val CHANNEL_ID = 2860
        const val NOTIFICATION_ACTION = "com.calldorado.preonboarding"
        lateinit var context: Context
        private lateinit var notificationManager: NotificationManager
        private lateinit var androidNotificationManagerCompat: NotificationManagerCompat
        private lateinit var androidNotificationManager: android.app.NotificationManager

        fun initialize(ctx: Context): NotificationManager {
            context = ctx.applicationContext
            notificationManager = NotificationManager(ctx)
            if (!Utils.isCalldoradoInstalled()) {
                getInstance().displayUpdateNotification()
            }
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
    }

    fun displayUpdateNotification() {
        if (Utils.isSunday()) {
            Timber.d("It's sunday! Relax")
            return
        }
        if (!Utils.isWithinTimeframe() && !BuildConfig.DEBUG) {
            Timber.d("Not the right time to display notifications (only between 15-18)")
            return
        }

        createNotificationChannel()
        var builder = NotificationCompat.Builder(context, "$CHANNEL_ID")
            .setSmallIcon(R.drawable.baseline_update_24)
            .setContentTitle(context.getString(R.string.preonb_notification_header))
            .setContentText(context.getString(R.string.preonb_notification_body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent())
            .setSound(getSoundUri())
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        androidNotificationManagerCompat.notify(CHANNEL_ID, builder.build())
        getSoundUri()
    }

    private fun getSoundUri(): Uri {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        Timber.d("Sound URI $soundUri")
        return soundUri
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.preonb_notification_channel_name)
            val descriptionText = context.getString(R.string.preonb_notification_body)
            val importance = android.app.NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("$CHANNEL_ID", name, importance).apply {
                description = descriptionText
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
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
            Intent(context, UpdateActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //or Intent.FLAG_ACTIVITY_SINGLE_TOP
                action = NOTIFICATION_ACTION
            }

        Timber.d("Intent to launch ${intent.toString()} action: ${intent.action}")
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, NOTIFICATION_REQ_CODE, intent as Intent, FLAG_UPDATE_CURRENT)
        return pendingIntent
    }

    fun dismissNotification() {
        androidNotificationManager.cancel(CHANNEL_ID)
    }
}
