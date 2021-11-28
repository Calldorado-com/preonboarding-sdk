package com.calldorado.preonboarding

import android.util.Log
import timber.log.Timber

class ReleaseTree: Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Don't log VERBOSE
        if (priority == Log.VERBOSE) {
            return
        } else {
            Log.println(priority, tag, message);
        }


        if (priority == Log.ERROR){
            val throwable = t ?: Exception(message)

            // TODO report to Doralytics or Firebase?
        }

    }

}