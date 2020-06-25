package com.jumpitt.happ.ui.registerLivingPlace

import android.app.Activity
import com.jumpitt.happ.ui.registerWorkPlace.RegisterWorkplace
import com.jumpitt.happ.utils.goToActivity

class RegisterLivingPlaceRouter constructor(private val activity: Activity): RegisterLivingPlaceContract.Router{

    override fun navigateRegisterWorkplace() {
        activity.goToActivity<RegisterWorkplace>()
    }

}