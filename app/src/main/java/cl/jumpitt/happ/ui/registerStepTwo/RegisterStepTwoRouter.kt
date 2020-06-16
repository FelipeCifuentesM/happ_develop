package cl.jumpitt.happ.ui.registerStepTwo

import android.app.Activity
import cl.jumpitt.happ.ui.registerLivingPlace.RegisterLivingPlace
import cl.jumpitt.happ.utils.goToActivity

class RegisterStepTwoRouter constructor(private val activity: Activity): RegisterStepTwoContract.Router{
    override fun navigateRegisterLivingPlace() {
        activity.goToActivity<RegisterLivingPlace>()
    }


}