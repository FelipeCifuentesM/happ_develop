package cl.jumpitt.happ.ui.registerWorkPlace

import android.app.Activity
import cl.jumpitt.happ.ui.registerPermissions.RegisterPermissions
import cl.jumpitt.happ.utils.goToActivity

class RegisterWorkPlaceRouter constructor(private val activity: Activity): RegisterWorkPlaceContract.Router{

    override fun navigateRegisterPermissions() {
        activity.goToActivity<RegisterPermissions>()
    }

}