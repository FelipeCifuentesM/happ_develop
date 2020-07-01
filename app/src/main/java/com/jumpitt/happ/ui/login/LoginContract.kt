package com.jumpitt.happ.ui.login

import com.jumpitt.happ.network.request.LoginAccessTokenRequest
import com.jumpitt.happ.network.response.LoginAccessTokenResponse
import com.jumpitt.happ.network.response.ProfileResponse
import com.jumpitt.happ.realm.RegisterData
import retrofit2.Response

interface LoginContract {
    interface View{
        fun showInitializeView()
        fun showValidateLoginError(messageError: String)
        fun showLoader()
        fun hideLoader()
    }

    interface Presenter{
        fun initializeView()
        fun navigateRegisterStepOne()
        fun postLoginAccessToken(loginRequest: LoginAccessTokenRequest, requestPermissions: Boolean)
        fun navigateRecoverPass()
    }

    interface Interactor{
        fun postLoginAccessToken(loginRequest: LoginAccessTokenRequest, interactorOutput: InteractorOutputs)
        fun getProfile(dataResponseToken: LoginAccessTokenResponse, interactorOutput: InteractorOutputs)
        fun saveRegisterProfile(userRealm: RegisterData)
    }

    interface Router{
        fun navigateRegisterStepOne()
        fun navigateMain()
        fun navigateRecoverPass()
    }

    interface InteractorOutputs{
        fun postLoginAccessTokenOutput(dataResponseToken: LoginAccessTokenResponse)
        fun postLoginAccessTokenOutputError(errorCode: Int, response: Response<LoginAccessTokenResponse>)
        fun getProfileOutput(dataLoginResponse: ProfileResponse, accessToken: String, refreshToken: String)
        fun getProfileOutputError(errorCode: Int, response: Response<ProfileResponse>)
        fun LoginFailureError()
    }
}