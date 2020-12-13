package com.jumpitt.happ.ui.profile

import androidx.fragment.app.FragmentActivity
import com.jumpitt.happ.model.ProfileMenu
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.ui.login.LoginContract

interface ProfileFragmentContract {
    interface View{
        fun initDataListProfile()
        fun showInitializeView(profileMenuListObject: ArrayList<ProfileMenu>)
        fun showUnwrappingValues(userData: RegisterData?)
        fun showSnackBar(message: String)
        fun stopHandlerPingActiveUser()
    }

    interface Presenter{
        fun initializeView()
        fun showInitializeView(profileMenuListObject: ArrayList<ProfileMenu>)
        fun getUserProfileData()
        fun navigateTracingLog()
        fun deleteProfileData()
        fun clickListenerItemProfileMenu(position: Int)
        fun navigateChangePassword(fragment: ProfileFragment, activity: FragmentActivity?)
        fun navigatePrivacyPolicies()
        fun navigateFrequentQuestions()
    }

    interface Interactor{
        fun getUserProfileData()
        fun deleteRegisterTokenFCM(accessToken: String, tokenFCMRequest: TokenFCMRequest)
        fun getAccessToken()
        fun deleteProfileData()
    }

    interface Router{
        fun navigateTracingLog()
        fun navigateLogin()
        fun navigateChangePassword(fragment: ProfileFragment, activity: FragmentActivity?)
        fun navigateWebView(urlWebView: String, titleBar: String)
    }

    interface InteractorOutputs{
        fun getUserProfileDataOutput(userData: RegisterData?)
        fun getAccesTokenOutput(accessToken: String)
        fun postRegisterTokenFCMFailureError()
        fun postRegisterTokenFCMOutput()
    }
}