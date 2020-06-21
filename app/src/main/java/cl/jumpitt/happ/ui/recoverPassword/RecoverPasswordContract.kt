package cl.jumpitt.happ.ui.recoverPassword

import cl.jumpitt.happ.network.request.RecoverPasswordRequest
import cl.jumpitt.happ.network.response.RecoverPasswordResponse
import retrofit2.Response

interface RecoverPasswordContract {
    interface View{
        fun showInitializeView()
        fun showRecoverPasswordResponse(message: String?)
        fun showLoader()
        fun hideLoader()
    }

    interface Presenter{
        fun initializeView()
        fun postForgotPassword(email: String)

    }

    interface Interactor{
        fun postForgotPassword(recoverPasswordRequest: RecoverPasswordRequest)
    }

    interface Router{

    }

    interface InteractorOutputs{
        fun postRecoverPassOutput(response: RecoverPasswordResponse)
        fun postRecoverPassOutputError(errorCode: Int, response: Response<RecoverPasswordResponse>)
        fun postRecoverPassFailureError()
    }

}