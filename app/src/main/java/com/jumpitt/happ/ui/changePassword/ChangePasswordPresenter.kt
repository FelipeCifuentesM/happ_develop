package com.jumpitt.happ.ui.changePassword

import android.app.Activity
import com.jumpitt.happ.R
import com.jumpitt.happ.network.request.ChangePasswordRequest
import com.jumpitt.happ.network.response.ChangePasswordResponse
import com.jumpitt.happ.utils.qualifyResponseErrorDefault
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
        mInteractor.putUpdatePassword("Bearer $accessToken", changePasswordRequest)
    }

    override fun getRecoverPassOutput(changePasswordResponse: ChangePasswordResponse) {
        mView.hideLoader()
        mRouter.navigateReturnProfile()
//        mView.showChangePasswordResponse(activity.resources.getString(R.string.snkChangePassSuccess))


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