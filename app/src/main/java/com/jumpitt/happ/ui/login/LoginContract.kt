package com.jumpitt.happ.ui.login

import com.jumpitt.happ.network.request.LoginAccessTokenRequest
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.network.response.LoginAccessTokenResponse
import com.jumpitt.happ.network.response.PingActiveUserResponse
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
        fun validateBluetoothState(userRealm: RegisterData)
        fun deleteProfileData()
    }

    interface Interactor{
        fun postLoginAccessToken(loginRequest: LoginAccessTokenRequest, interactorOutput: InteractorOutputs)
        fun getProfile(dataResponseToken: LoginAccessTokenResponse, interactorOutput: InteractorOutputs)
        fun saveRegisterProfile(userRealm: RegisterData)
        fun postRegisterTokenFCM(accessToken: String, tokenFCMRequest: TokenFCMRequest, interactorOutput: InteractorOutputs)
        fun getPingUserActive(accessToken: String, interactorOutput: InteractorOutputs)
        fun getAccessToken(interactorOutput: InteractorOutputs)
        fun deleteProfileData()
    }

    interface Router{
        fun navigateRegisterStepOne()
        fun navigateMain()
        fun navigateRecoverPass()
        fun navigatePermissionBluetooth()
    }

    interface InteractorOutputs{
        fun postLoginAccessTokenOutput(dataResponseToken: LoginAccessTokenResponse)
        fun postLoginAccessTokenOutputError(errorCode: Int, response: Response<LoginAccessTokenResponse>)
        fun getProfileOutput(dataLoginResponse: ProfileResponse, accessToken: String, refreshToken: String)
        fun getProfileOutputError(errorCode: Int, response: Response<ProfileResponse>)
        fun postRegisterTokenFCMFailureError(accessToken: String)
        fun postRegisterTokenFCMOutput(accessToken: String)
        fun loginFailureError()
        fun getPingUserActiveOutput(dataPingResponse: PingActiveUserResponse)
        fun getPingUserActiveOutputError(dataPingResponse: PingActiveUserResponse, responseCode: Int)
        fun getPingUserActiveFailureError(dataPingResponse: PingActiveUserResponse)
        fun getAccessTokenOutput(accessToken: String)
    }
}