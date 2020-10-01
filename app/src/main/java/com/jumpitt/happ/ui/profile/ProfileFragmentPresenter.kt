package com.jumpitt.happ.ui.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.utils.ConstantsApi


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
        mInteractor.getAccessToken()
    }

    override fun getAccesTokenOutput(accessToken: String) {
        mInteractor.deleteProfileData()
        //Get device id for notifications
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener(OnSuccessListener<InstanceIdResult> { instanceIdResult ->
                val mToken: String? = instanceIdResult.token
                mToken?.let {deviceToken ->
                    val tokenFCMRequest = TokenFCMRequest(deviceToken)
                    mInteractor.deleteRegisterTokenFCM("${ConstantsApi.BEARER} $accessToken", tokenFCMRequest)
                }?: run {
                    mView.stopHandlerPingActiveUser()
                    mRouter.navigateLogin()
                }
            })
    }

    override fun navigateChangePassword(fragment: ProfileFragment, activity: FragmentActivity?) {
        mRouter.navigateChangePassword(fragment, activity)
    }

    override fun getUserProfileDataOutput(userData: RegisterData?) {
        mView.showUnwrappingValues(userData)
    }

    override fun postRegisterTokenFCMFailureError() {
        mView.stopHandlerPingActiveUser()
        mRouter.navigateLogin()
    }

    override fun postRegisterTokenFCMOutput() {
        mView.stopHandlerPingActiveUser()
        mRouter.navigateLogin()
    }

}