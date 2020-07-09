package com.jumpitt.happ.ui.main

import android.app.Activity
import android.content.Intent
import com.jumpitt.happ.ui.registerPermissions.RegisterPermissions


class MainActivityRouter constructor(private val activity: Activity): MainActivityContract.Router{
    override fun navigateBluetoothPermission() {
        val bluetoothDisabledIntent = Intent(activity, RegisterPermissions::class.java)
        bluetoothDisabledIntent.putExtra("validateReturnWhitOutPermission", true)
        bluetoothDisabledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.applicationContext.startActivity(bluetoothDisabledIntent)
    }


}