package com.jumpitt.happ.ui.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.jumpitt.happ.BuildConfig.*
import com.jumpitt.happ.R
import com.jumpitt.happ.model.ProfileMenu
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.utils.ConstantsApi


class ProfileFragmentPresenter constructor(val fragment: Fragment): ProfileFragmentContract.Presenter, ProfileFragmentContract.InteractorOutputs{
    private var mView: ProfileFragmentContract.View = fragment as ProfileFragmentContract.View
    private var mRouter: ProfileFragmentContract.Router = ProfileFragmentRouter(fragment)
    private var mInteractor: ProfileFragmentContract.Interactor = ProfileFragmentInteractor(this)

    override fun initializeView() {
        mView.initDataListProfile()
    }

    override fun showInitializeView(profileMenuListObject: ArrayList<ProfileMenu>) {
        mView.showInitializeView(profileMenuListObject)
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

    override fun clickListenerItemProfileMenu(position: Int) {
        when(position){
            0 -> {
                val urlWebView = URL_PRIVACY_POLICIES_WEB
                val titleBar = fragment.resources.getString(R.string.tbPrivacy)
                mRouter.navigateWebView(urlWebView, titleBar)
            }
            1 -> {
                val urlWebView = URL_FRECUENT_QUESTIONS_WEB
                val titleBar = fragment.resources.getString(R.string.tbFrequentQuestions)
                mRouter.navigateWebView(urlWebView, titleBar)
            }
            2 -> {
                mInteractor.getAccessToken()
            }
        }
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

    override fun navigatePrivacyPolicies() {
        val urlWebView = URL_PRIVACY_POLICIES_WEB
        val titleBar = fragment.resources.getString(R.string.tbPrivacy)
        mRouter.navigateWebView(urlWebView, titleBar)
    }

    override fun navigateFrequentQuestions() {
        val urlWebView = URL_FRECUENT_QUESTIONS_WEB
        val titleBar = fragment.resources.getString(R.string.tbFrequentQuestions)
        mRouter.navigateWebView(urlWebView, titleBar)
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