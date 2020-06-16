package cl.jumpitt.happ.ui.profile

import androidx.fragment.app.Fragment
import cl.jumpitt.happ.ui.TracingLogActivity
import cl.jumpitt.happ.utils.goToActivity

class ProfileFragmentRouter constructor(private val mFragment: Fragment): ProfileFragmentContract.Router{
    override fun navigateTracingLog() {
        mFragment.activity?.goToActivity<TracingLogActivity>()
    }

}