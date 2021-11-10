package com.calldorado.preonboarding

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.calldorado.preonboarding.databinding.ActivityUpdateBinding
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED

class UpdateActivity : AppCompatActivity() {

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
        }
    }
}