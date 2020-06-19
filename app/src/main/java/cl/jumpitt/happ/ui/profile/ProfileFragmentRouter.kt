package cl.jumpitt.happ.ui.profile

import android.content.Intent
import androidx.fragment.app.Fragment
import cl.jumpitt.happ.ui.TracingLogActivity
import cl.jumpitt.happ.ui.changePassword.ChangePasswordActivity
import cl.jumpitt.happ.ui.login.Login
import cl.jumpitt.happ.utils.goToActivity

class ProfileFragmentRouter constructor(private val mFragment: Fragment): ProfileFragmentContract.Router{
    override fun navigateTracingLog() {
        mFragment.activity?.goToActivity<TracingLogActivity>()
    }

    override fun navigateLogin() {
        mFragment.activity?.goToActivity<Login>("") {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    override fun navigateChangePassword() {
        mFragment.activity?.goToActivity<ChangePasswordActivity>()
    }

}