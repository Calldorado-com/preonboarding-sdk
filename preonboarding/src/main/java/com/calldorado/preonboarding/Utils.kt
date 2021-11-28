package com.calldorado.preonboarding

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import org.joda.time.DateTime
import org.joda.time.Duration
import timber.log.Timber
import java.util.*


class Utils {
    companion object {
        fun getMetadataLaunchClass(context: Context): String? {
            context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            ).apply {
                metaData?.let {
                    val launchClass =
                        metaData.getString("com.calldorado.preonboarding.launch_class")
                    return launchClass
                }
                return null
            }
        }

        fun getMetadataTestTime(context: Context): Long {
            context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            ).apply {
                metaData?.let {
                    try {
                        val testTime =
                            metaData.get("com.calldorado.preonboarding.test_time_minutes")
                        return "$testTime".toLong()
                    } catch (e: Exception){
                        return -1
                    }
                }
                return -1
            }
        }

        fun getMetadataValue(context: Context, key: String): Any? {
            context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            ).apply {
                metaData?.let {
                    try {
                        return metaData.get(key)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                return -1
            }
        }

        fun isSunday(): Boolean {
            val calendar: Calendar = Calendar.getInstance()
            val day: Int = calendar.get(Calendar.DAY_OF_WEEK)

            return when (day) {
                Calendar.SUNDAY -> true
                else -> false
            }
        }

        fun isWithinTimeframe(context: Context): Boolean {
            //Bypass when in Debug mode
            if (BuildConfig.DEBUG)
                return true

            //Bypass when test metadata is set
            val isTestTimeSet = (Utils.getMetadataTestTime(context) != -1L)
            if (isTestTimeSet)
                return true

            val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            return currentTime in 15..18
        }

        fun isCalldoradoInstalled(): Boolean {
            try {
                var calldoradoClassObjectName = if (BuildConfig.DEBUG) "com.calldorado.app.Calldorado" else "com.calldorado.Calldorado"
                val calldoradoClassObject = Class.forName(calldoradoClassObjectName).kotlin.objectInstance
                return calldoradoClassObject != null
            } catch (e: Exception){
                return false
            }
        }

        fun getMinutesUntilHour(context: Context, hour24Format: Int): Long {
            /*
                If a test time is set in metadata, use that
             */
            val testTime = getMetadataTestTime(context)
            if (testTime != -1L){
                return testTime
            }

            /*
                If we are already within the timeframe(between 15-18), return 0 delay
             */
            if (isWithinTimeframe(context)){
                return 0L
            }

            var delay = Duration(
                DateTime.now(),
                DateTime.now().withTimeAtStartOfDay().plusDays(1).plusHours(hour24Format)
            ).standardMinutes

            if (DateTime.now().hourOfDay < hour24Format) {
                delay = Duration(
                    DateTime.now(),
                    DateTime.now().withTimeAtStartOfDay().plusHours(hour24Format)
                ).standardMinutes
            }
            Timber.d("Delay in minutes are $delay")
            return delay
        }

        fun getUpdateAvailabilityStatus(availibility: Int): String {
            when (availibility){
//                0 -> return "UNKNOWN"
                1 -> return "UPDATE_NOT_AVAILABLE"
                2 -> return "UPDATE_AVAILABLE"
                3 -> return "DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS"
                else -> return "UNKNOWN"
            }
        }

        fun getAppIcon(context: Context): Int {
            try {
                context.resources.getIdentifier(getMetadataValue(context, "com.calldorado.preonboarding.notification_icon_res_name") as String,
                    "drawable",
                    context.packageName).let {
                    Timber.d("Icon is $it")
                    if (it != 0)
                        return it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
           return R.drawable.baseline_update_24
        }

        fun getAppIconLarge(context: Context): Bitmap? {
            val iconDrawable = context.packageManager.getApplicationIcon(context.packageName)

            return Bitmap.createBitmap(
                iconDrawable.intrinsicWidth,
                iconDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
    }
}