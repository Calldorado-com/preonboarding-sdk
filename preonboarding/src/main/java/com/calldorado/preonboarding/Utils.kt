package com.calldorado.preonboarding

import android.content.Context
import android.content.pm.PackageManager

class Utils {
    companion object{
        fun getMetadataLaunchClass(context: Context): String? {
            context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            ).apply {
                metaData?.let {
                    val launchClass = metaData.getString("com.calldorado.preonboarding.launch_class")
                    return launchClass
                }
                return null
            }
        }
    }
}