package com.calldorado.preonboarding

import android.content.Context
import android.content.pm.PackageManager
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
    }
}