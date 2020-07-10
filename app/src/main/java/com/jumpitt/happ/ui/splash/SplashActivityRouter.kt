package com.jumpitt.happ.ui.splash

import android.app.Activity
import android.content.Intent
import com.jumpitt.happ.ui.OnBoard
import com.jumpitt.happ.ui.main.MainActivity
import com.jumpitt.happ.ui.registerPermissions.RegisterPermissions
import com.jumpitt.happ.utils.goToActivity

class SplashActivityRouter constructor(private val activity: Activity): SplashActivityContract.Router{
    override fun navigateMainActivity() {
        activity.goToActivity<MainActivity>("") {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        activity.finish()
    }

    override fun navigateOnBoard() {
        activity.goToActivity<OnBoard>("")
        activity.finish()
    }

    override fun navigatePermissionBluetooth() {
        val bluetoothDisabledIntent = Intent(activity, RegisterPermissions::class.java)
        bluetoothDisabledIntent.putExtra("fromSplash", true)
        bluetoothDisabledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(bluetoothDisabledIntent)
        activity.finish()
    }
}