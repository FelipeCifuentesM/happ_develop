package com.jumpitt.happ.ui.registerWorkPlace

import android.app.Activity
import com.jumpitt.happ.ui.registerPermissions.RegisterPermissions
import com.jumpitt.happ.utils.goToActivity

class RegisterWorkPlaceRouter constructor(private val activity: Activity): RegisterWorkPlaceContract.Router{

    override fun navigateRegisterPermissions() {
        activity.goToActivity<RegisterPermissions>()
    }

}