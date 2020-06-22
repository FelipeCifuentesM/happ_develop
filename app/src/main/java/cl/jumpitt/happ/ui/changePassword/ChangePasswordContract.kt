package cl.jumpitt.happ.ui.changePassword

import cl.jumpitt.happ.network.request.ChangePasswordRequest
import cl.jumpitt.happ.network.response.ChangePasswordResponse
import retrofit2.Response

interface ChangePasswordContract {
    interface View{
        fun showInitializeView()
        fun showChangePasswordResponse(messageError: String)
        fun showLoader()
        fun hideLoader()
    }

    interface Presenter{
        fun initializeView()
        fun getAccessTokenProfile(oldPassword: String, newPassword: String)
    }

    interface Interactor{
        fun getAccessTokenProfile(changePasswordRequest: ChangePasswordRequest)
        fun putUpdatePassword(accessToken: String, changePasswordRequest: ChangePasswordRequest)
    }

    interface Router{
        fun navigateReturnProfile()
    }

    interface InteractorOutputs{
        fun getAccessTokenProfileOutput(accessToken: String, changePasswordRequest: ChangePasswordRequest)
        fun getRecoverPassFailureError()
        fun getRecoverPassOutput(changePasswordResponse: ChangePasswordResponse)
        fun getRecoverPassOutputError(errorCode: Int, response: Response<ChangePasswordResponse>)
    }
}