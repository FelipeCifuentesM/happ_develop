package com.jumpitt.happ.ui.changePassword

import android.app.Activity
import android.content.Intent
import com.jumpitt.happ.utils.RequestCode


class ChangePasswordRouter constructor(private val activity: Activity): ChangePasswordContract.Router {

    override fun navigateReturnProfile() {
        val returnIntent = Intent()
        activity.setResult(RequestCode.FROM_PROFILE_FRAGMENT, returnIntent)
        activity.finish()
    }


}