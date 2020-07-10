package com.jumpitt.happ.ui.login

import android.app.Activity
import android.content.Intent
import com.jumpitt.happ.ui.recoverPassword.RecoverPasswordActivity
import com.jumpitt.happ.ui.main.MainActivity
import com.jumpitt.happ.ui.registerPermissions.RegisterPermissions
import com.jumpitt.happ.ui.registerStepOne.RegisterStepOne
import com.jumpitt.happ.utils.goToActivity

class LoginRouter constructor(private val activity: Activity): LoginContract.Router{

    override fun navigateMain(){
        activity.goToActivity<MainActivity>(""){
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

    }

    override fun navigateRecoverPass() {
        activity.goToActivity<RecoverPasswordActivity>()
    }

    override fun navigatePermissionBluetooth() {
        val bluetoothDisabledIntent = Intent(activity, RegisterPermissions::class.java)
        bluetoothDisabledIntent.putExtra("fromLogin", true)
        bluetoothDisabledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(bluetoothDisabledIntent)
    }

    override fun navigateRegisterStepOne() {
        activity.goToActivity<RegisterStepOne>()
    }

}