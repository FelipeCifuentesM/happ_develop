package com.jumpitt.happ.ui.registerPermissions

import android.app.Activity
import android.content.Intent
import com.jumpitt.happ.ui.main.MainActivity
import com.jumpitt.happ.ui.registerSuccess.RegisterSuccess
import com.jumpitt.happ.utils.goToActivity

class RegisterPermissionsRouter constructor(private val activity: Activity): RegisterPermissionsContract.Router{

    override fun navigateRegisterSuccess() {
        activity.goToActivity<RegisterSuccess>()
    }

    override fun navigateMainActivity() {
        activity.goToActivity<MainActivity>(""){
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

}