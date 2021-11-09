package com.calldorado.preonboarding

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.calldorado.preonboarding.databinding.ActivityUpdateBinding
import com.google.android.play.core.appupdate.AppUpdateInfo

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
        binding.buttonExit.setOnClickListener({
            finish()
        })
    }

}