package cl.jumpitt.happ.ui.registerPermissions

import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.response.RegisterResponse
import retrofit2.Response

interface RegisterPermissionsContract {
    interface View{
        fun showInitializeView()
        fun showRegisterError(messageError: String)
    }

    interface Presenter{
        fun initializeView()
        fun navigateRegisterSuccess()
        fun getRegisterData()
    }

    interface Interactor{
        fun getRegisterData(interactorOutputs: InteractorOutputs)
        fun postRegister(registerRequest: RegisterRequest, interactorOutputs: InteractorOutputs)
        fun saveRegisterProfile(dataRegisterResponse: RegisterResponse)
    }

    interface Router{
        fun navigateRegisterSuccess()
    }

    interface InteractorOutputs{
        fun getRegisterDataOutput(registerData: RegisterRequest)
        fun postRegisterOutput(dataRegisterResponse: RegisterResponse)
        fun postRegisterOutputError(errorCode: Int, response: Response<RegisterResponse>)
        fun postRegisterFailureError()

    }
}