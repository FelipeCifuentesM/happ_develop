package cl.jumpitt.happ.ui.login

import android.app.Activity
import android.content.Intent
import cl.jumpitt.happ.ui.main.MainActivity
import cl.jumpitt.happ.ui.registerStepOne.RegisterStepOne
import cl.jumpitt.happ.utils.goToActivity

class LoginRouter constructor(private val activity: Activity): LoginContract.Router{

    override fun navigateMain(){
        activity.goToActivity<MainActivity>(""){
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

    }

    override fun navigateRegisterStepOne() {
        activity.goToActivity<RegisterStepOne>()
    }

}