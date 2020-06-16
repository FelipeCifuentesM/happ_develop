package cl.jumpitt.happ.ui.registerStepOne

import android.app.Activity
import cl.jumpitt.happ.ui.registerStepTwo.RegisterStepTwo
import cl.jumpitt.happ.utils.goToActivity

class RegisterStepOneRouter constructor(private val activity: Activity): RegisterStepOneContract.Router{
    override fun navigateRegisterStepTwo() {
        activity.goToActivity<RegisterStepTwo>()
    }

}