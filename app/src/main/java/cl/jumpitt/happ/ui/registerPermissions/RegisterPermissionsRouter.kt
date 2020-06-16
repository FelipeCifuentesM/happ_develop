package cl.jumpitt.happ.ui.registerPermissions

import android.app.Activity
import cl.jumpitt.happ.ui.registerSuccess.RegisterSuccess
import cl.jumpitt.happ.utils.goToActivity

class RegisterPermissionsRouter constructor(private val activity: Activity): RegisterPermissionsContract.Router{

    override fun navigateRegisterSuccess() {
        activity.goToActivity<RegisterSuccess>()
    }

}