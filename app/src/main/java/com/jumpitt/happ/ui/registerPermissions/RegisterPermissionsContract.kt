package com.jumpitt.happ.ui.registerPermissions

import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.realm.RegisterData
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
    }

    interface Interactor{
        fun getRegisterData(interactorOutputs: InteractorOutputs)
        fun postRegister(registerRequest: RegisterRequest, interactorOutputs: InteractorOutputs)
        fun saveRegisterProfile(userRealm: RegisterData)
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
    }
}