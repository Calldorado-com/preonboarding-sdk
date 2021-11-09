package com.calldorado.preonboarding.update

import android.app.Activity
import android.content.Context
import com.calldorado.preonboarding.PreonboardingApi.UPDATE_REQUEST_CODE
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import timber.log.Timber
import java.lang.Exception

class UpdateManager private constructor(activity: Activity) {

    companion object {
        val TAG = UpdateManager::class.simpleName
        lateinit var activity1: Activity
        private lateinit var instance: UpdateManager

        fun getInstance(activity: Activity): UpdateManager {
            UpdateManager.instance = UpdateManager(activity)
            UpdateManager.activity1 = activity
            return instance
        }

        fun updateImmediately(instance: UpdateManager, statusOnUpdate : (Any) -> Unit) {
            Timber.d("Running immediate update")
            val UPDATE_TYPE = AppUpdateType.IMMEDIATE
            val appUpdateManager = AppUpdateManagerFactory.create(activity1)

            // Returns an intent object that you use to check for an update.
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo

            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                Timber.d("Update success ${appUpdateInfo.availableVersionCode()}")
                statusOnUpdate("Update succeeded:\n${appUpdateInfo.toString()}")
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(UPDATE_TYPE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        UPDATE_TYPE,
                        activity1,
                        UPDATE_REQUEST_CODE
                    )
                }
            }
            appUpdateInfoTask.addOnFailureListener { appUpdateInfo ->
                Timber.d("Update failed ${appUpdateInfo.toString()}")
                statusOnUpdate("Update failed:\n${appUpdateInfo.toString()}")
            }

            appUpdateInfoTask.addOnCompleteListener{ appUpdateInfo ->
                Timber.d("Update completed ${appUpdateInfo.toString()}")
                /*try {
                appUpdateInfo.result //This throws an exception
                    statusOnUpdate("isComplete ${appUpdateInfo.result}") //${appUpdateInfo.result} //${appUpdateInfo.isSuccessful} isComplete

                } catch (e: Exception){
                    e.printStackTrace()
                }*/
            }
        }

        fun updateFlex(instance: UpdateManager) {
            val appUpdateManager = AppUpdateManagerFactory.create(activity1)
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                ) {
                    //TODO
                }
            }
        }
    }
}