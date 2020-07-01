package com.jumpitt.happ.ui.recoverPassword

import com.jumpitt.happ.network.request.RecoverPasswordRequest
import com.jumpitt.happ.network.response.RecoverPasswordResponse
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
        fun postForgotPassword(contentType: String, email: String)
    }

    interface Router{

    }

    interface InteractorOutputs{
        fun postRecoverPassOutput(response: RecoverPasswordResponse)
        fun postRecoverPassOutputError(errorCode: Int, response: Response<RecoverPasswordResponse>)
        fun postRecoverPassFailureError()
    }

}