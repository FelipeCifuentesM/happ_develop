package cl.jumpitt.happ.ui.changePassword

import android.app.Activity


class ChangePasswordRouter constructor(private val activity: Activity): ChangePasswordContract.Router {

    override fun navigateReturnProfile() {
        activity.finish()
    }


}