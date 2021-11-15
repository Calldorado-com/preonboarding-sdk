package com.calldorado.preonboarding

import android.content.Context
import android.content.pm.PackageManager
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

        fun isSunday(): Boolean {
            val calendar: Calendar = Calendar.getInstance()
            val day: Int = calendar.get(Calendar.DAY_OF_WEEK)

            return when (day) {
                Calendar.SUNDAY -> true
                else -> false
            }
        }

        fun isWithinTimeframe(): Boolean {
            val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            return currentTime in 15..18
        }

        fun isCalldoradoInstalled(): Boolean {
            try {
                var calldoradoClassObjectName = if (BuildConfig.DEBUG) "com.calldorado.preonboarding.Calldorado" else "com.calldorado.Calldorado"
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
            if (isWithinTimeframe()){
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
    }
}