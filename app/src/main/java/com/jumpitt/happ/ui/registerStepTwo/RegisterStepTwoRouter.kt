package com.jumpitt.happ.ui.registerStepTwo

import android.app.Activity
import com.jumpitt.happ.ui.registerLivingPlace.RegisterLivingPlace
import com.jumpitt.happ.utils.goToActivity

class RegisterStepTwoRouter constructor(private val activity: Activity): RegisterStepTwoContract.Router{
    override fun navigateRegisterLivingPlace() {
        activity.goToActivity<RegisterLivingPlace>()
    }


}