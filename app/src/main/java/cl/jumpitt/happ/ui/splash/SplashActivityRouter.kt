package cl.jumpitt.happ.ui.splash

import android.app.Activity
import android.content.Intent
import cl.jumpitt.happ.ui.OnBoard
import cl.jumpitt.happ.ui.main.MainActivity
import cl.jumpitt.happ.utils.goToActivity

class SplashActivityRouter constructor(private val activity: Activity): SplashActivityContract.Router{
    override fun navigateMainActivity() {
        activity.goToActivity<MainActivity>("") {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        activity.finish()
    }

    override fun navigateOnBoard() {
        activity.goToActivity<OnBoard>("")
        activity.finish()
    }

}