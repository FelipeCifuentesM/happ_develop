package com.jumpitt.happ.ui.registerStepOne

import android.app.Activity
import com.jumpitt.happ.ui.registerStepTwo.RegisterStepTwo
import com.jumpitt.happ.utils.goToActivity

class RegisterStepOneRouter constructor(private val activity: Activity): RegisterStepOneContract.Router{
    override fun navigateRegisterStepTwo() {
        activity.goToActivity<RegisterStepTwo>()
    }

}