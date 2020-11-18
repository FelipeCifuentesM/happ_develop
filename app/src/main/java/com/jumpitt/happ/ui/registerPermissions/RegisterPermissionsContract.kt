package com.jumpitt.happ.ui.registerPermissions

import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.network.response.PingActiveUserResponse
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.ui.login.LoginContract
import retrofit2.Response

interface RegisterPermissionsContract {
    interface View{
        fun showInitializeView()
        fun showRegisterError(messageError: String)
        fun showLoader()
        fun hideLoader()

    }

    interface Presenter{
        fun initializeView()
        fun navigateMainActivity()
        fun getRegisterData(requestPermissions: Boolean)
        fun validateTcn()
        fun runPingActiveUser()
    }

    interface Interactor{
        fun getRegisterData(interactorOutputs: InteractorOutputs)
        fun postRegister(registerRequest: RegisterRequest, interactorOutputs: InteractorOutputs)
        fun saveRegisterProfile(userRealm: RegisterData)
        fun postRegisterTokenFCM(accessToken: String, tokenFCMRequest: TokenFCMRequest, interactorOutputs: InteractorOutputs)
        fun getAccessToken(interactorOutputs: InteractorOutputs)
        fun getPingUserActive(accessToken: String, interactorOutput: InteractorOutputs)
    }

    interface Router{
        fun navigateRegisterSuccess()
        fun navigateMainActivity()
    }

    interface InteractorOutputs{
        fun getRegisterDataOutput(registerData: RegisterRequest?)
        fun postRegisterOutput(dataRegisterResponse: RegisterResponse)
        fun postRegisterOutputError(errorCode: Int, response: Response<RegisterResponse>)
        fun postRegisterFailureError()
        fun postRegisterTokenFCMFailureError(accessToken: String)
        fun postRegisterTokenFCMOutput(accessToken: String)
        fun getAccessTokenOutput(accessToken: String)
        fun getPingUserActiveOutput(dataPingResponse: PingActiveUserResponse)
        fun getPingUserActiveOutputError(dataPingResponse: PingActiveUserResponse, responseCode: Int)
        fun getPingUserActiveFailureError(dataPingResponse: PingActiveUserResponse)

    }
}