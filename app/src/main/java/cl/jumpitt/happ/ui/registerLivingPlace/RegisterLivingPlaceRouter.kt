package cl.jumpitt.happ.ui.registerLivingPlace

import android.app.Activity
import cl.jumpitt.happ.ui.registerWorkPlace.RegisterWorkplace
import cl.jumpitt.happ.utils.goToActivity

class RegisterLivingPlaceRouter constructor(private val activity: Activity): RegisterLivingPlaceContract.Router{

    override fun navigateRegisterWorkplace() {
        activity.goToActivity<RegisterWorkplace>()
    }

}