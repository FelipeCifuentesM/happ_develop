package cl.jumpitt.happ.ui.login

import cl.jumpitt.happ.network.request.LoginAccessTokenRequest
import cl.jumpitt.happ.network.response.LoginAccessTokenResponse
import cl.jumpitt.happ.network.response.ProfileResponse

interface LoginContract {
    interface View{
        fun showInitializeView()

    }

    interface Presenter{
        fun initializeView()
        fun navigateRegisterStepOne()
        fun postLoginAccessToken(loginRequest: LoginAccessTokenRequest)
    }

    interface Interactor{
        fun postLoginAccessToken(loginRequest: LoginAccessTokenRequest, interactorOutput: InteractorOutputs)
        fun getProfile(dataResponseToken: LoginAccessTokenResponse, interactorOutput: InteractorOutputs)
        fun saveRegisterProfile(dataLoginResponse: ProfileResponse, accessToken: String)
    }

    interface Router{
        fun navigateRegisterStepOne()
        fun navigateMain()
    }

    interface InteractorOutputs{
        fun postLoginAccessTokenOutput(dataResponseToken: LoginAccessTokenResponse)
        fun postLoginAccessTokenOutputError()
        fun getProfileOutput(dataLoginResponse: ProfileResponse, accessToken: String)
        fun getProfileOutputError()
    }
}