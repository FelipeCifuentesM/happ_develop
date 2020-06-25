package com.jumpitt.happ.ui.registerPermissions

import android.app.Activity
import com.jumpitt.happ.ui.registerSuccess.RegisterSuccess
import com.jumpitt.happ.utils.goToActivity

class RegisterPermissionsRouter constructor(private val activity: Activity): RegisterPermissionsContract.Router{

    override fun navigateRegisterSuccess() {
        activity.goToActivity<RegisterSuccess>()
    }

}