package com.devshiv.drivesafetyapp

import android.app.Application
import com.devshiv.drivesafetyapp.utils.getAppVersion
import com.devshiv.drivesafetyapp.utils.getUserDeviceId
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        var curUser = ""
    }
}