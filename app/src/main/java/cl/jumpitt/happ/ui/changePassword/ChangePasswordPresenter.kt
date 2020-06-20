package cl.jumpitt.happ.ui.changePassword

import android.app.Activity
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.request.ChangePasswordRequest
import cl.jumpitt.happ.network.response.ChangePasswordResponse
import cl.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response

class ChangePasswordPresenter constructor(private val activity: Activity): ChangePasswordContract.Presenter, ChangePasswordContract.InteractorOutputs {
    private var mInteractor: ChangePasswordContract.Interactor = ChangePasswordInteractor(this)
    private var mView: ChangePasswordContract.View = activity as ChangePasswordContract.View
    private var mRouter: ChangePasswordContract.Router = ChangePasswordRouter(activity)

    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun getAccessTokenProfile(oldPassword: String, newPassword: String) {
        mView.showLoader()
        val changePasswordRequest = ChangePasswordRequest(oldPassword, newPassword)
        mInteractor.getAccessTokenProfile(changePasswordRequest)
    }

    override fun getAccessTokenProfileOutput(accessToken: String, changePasswordRequest: ChangePasswordRequest) {
        mInteractor.putUpdatePassword(accessToken, changePasswordRequest)
    }

    override fun getRecoverPassOutput(changePasswordResponse: ChangePasswordResponse) {
        mView.hideLoader()
    }

    override fun getRecoverPassFailureError(){
        mView.hideLoader()
        mView.showChangePasswordResponse(activity.resources.getString(R.string.snkDefaultApiError))
    }

    override fun getRecoverPassOutputError(errorCode: Int, response: Response<ChangePasswordResponse>) {
        mView.hideLoader()
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showChangePasswordResponse(messageError)
    }
}