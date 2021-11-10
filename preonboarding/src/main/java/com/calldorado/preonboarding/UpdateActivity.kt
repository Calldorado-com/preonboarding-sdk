package com.calldorado.preonboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.calldorado.preonboarding.databinding.ActivityUpdateBinding
import com.calldorado.preonboarding.notification.NotificationManager
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import timber.log.Timber

class UpdateActivity : AppCompatActivity() {
    companion object{
        const val NOTIFICATION_REQ_CODE = 2800
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        PreonboardingApi.updateImmediately(this) { statusOnUpdate ->
            binding.displayTxt.text = statusOnUpdate.toString()
        }
        binding.buttonExit.setOnClickListener {
            finish()
        }
        Timber.d("Started update activity")

        PreonboardingApi.dismissNotification()
        Timber.d("Has calldorado class: ${Utils.isCalldoradoInstalled()}")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("onNewIntent")
        if (intent?.action.equals(NotificationManager.NOTIFICATION_ACTION)){
            Timber.d("Notification action received")
            PreonboardingApi.dismissNotification()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("req: $requestCode res: $resultCode")
        when (requestCode) {
            PreonboardingApi.UPDATE_REQUEST_CODE -> {
                when (resultCode){
                    //The user has accepted the update. For immediate updates, you might not receive
                    // this callback because the update should already be finished by the time control
                    // is given back to your app.
                    RESULT_OK -> {
                        val intent = packageManager.getLaunchIntentForPackage(packageName)
                        startActivity(intent)
                        finish()
                    }

                    //The user has denied or canceled the update.
                    RESULT_CANCELED -> {
                        //TODO set notification timer as spec'ed
                    }

                    //Some other error prevented either the user from providing consent or the update from proceeding.
                    RESULT_IN_APP_UPDATE_FAILED -> {
                        //TODO set notification timer as spec'ed
                    }

                    else -> {
                        //TODO set notification timer as spec'ed
                    }
                }
            }

            NOTIFICATION_REQ_CODE -> PreonboardingApi.dismissNotification()
        }
    }
}