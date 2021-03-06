package cl.jumpitt.happ.ui.profile

import androidx.fragment.app.Fragment
import cl.jumpitt.happ.network.response.RegisterResponse

class ProfileFragmentPresenter constructor(val fragment: Fragment): ProfileFragmentContract.Presenter, ProfileFragmentContract.InteractorOutputs{
    private var mView: ProfileFragmentContract.View = fragment as ProfileFragmentContract.View
    private var mRouter: ProfileFragmentContract.Router = ProfileFragmentRouter(fragment)
    private var mInteractor: ProfileFragmentContract.Interactor = ProfileFragmentInteractor(this)

    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun getUserProfileData() {
        mInteractor.getUserProfileData()
    }

    override fun navigateTracingLog() {
        mRouter.navigateTracingLog()
    }

    override fun deleteProfileData() {
        mInteractor.deleteProfileData()
        mRouter.navigateLogin()
    }

    override fun navigateChangePassword() {
        mRouter.navigateChangePassword()
    }

    override fun getUserProfileDataOutput(userData: RegisterResponse) {
        mView.showUnwrappingValues(userData)
    }

}