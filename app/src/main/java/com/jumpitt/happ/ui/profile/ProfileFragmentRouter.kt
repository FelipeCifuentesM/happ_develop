package com.jumpitt.happ.ui.profile

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jumpitt.happ.ui.TracingLogActivity
import com.jumpitt.happ.ui.changePassword.ChangePasswordActivity
import com.jumpitt.happ.ui.login.Login
import com.jumpitt.happ.utils.goToActivity


class ProfileFragmentRouter constructor(private val mFragment: Fragment): ProfileFragmentContract.Router{
    override fun navigateTracingLog() {
        mFragment.activity?.goToActivity<TracingLogActivity>()
    }

    override fun navigateLogin() {
        mFragment.activity?.goToActivity<Login>("") {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    override fun navigateChangePassword(fragment: ProfileFragment, activity: FragmentActivity?) {
//        fragment.goToActivityForResult<ChangePasswordActivity>(requestCode = 2)
        val intent = Intent(activity, ChangePasswordActivity::class.java)
        fragment.startActivityForResult(intent, 2)

    }

}