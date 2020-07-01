package com.jumpitt.happ.ui.recoverPassword

import android.app.Activity
import com.jumpitt.happ.R
import com.jumpitt.happ.network.request.RecoverPasswordRequest
import com.jumpitt.happ.network.response.RecoverPasswordResponse
import com.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response

class RecoverPasswordPresenter constructor(private val activity: Activity): RecoverPasswordContract.Presenter, RecoverPasswordContract.InteractorOutputs {
    private var mInteractor: RecoverPasswordContract.Interactor = RecoverPasswordInteractor(this)
    private var mView: RecoverPasswordContract.View = activity as RecoverPasswordContract.View
    private var mRouter: RecoverPasswordContract.Router = RecoverPasswordRouter(activity)


    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun postForgotPassword(email: String) {
        mView.showLoader()
        mInteractor.postForgotPassword("application/x-www-form-urlencoded", email)
    }

    override fun postRecoverPassOutput(response: RecoverPasswordResponse) {
        mView.hideLoader()
        mView.showRecoverPasswordResponse(response.message)
    }

    override fun postRecoverPassOutputError(errorCode: Int, response: Response<RecoverPasswordResponse>) {
        mView.hideLoader()
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showRecoverPasswordResponse(messageError)
    }

    override fun postRecoverPassFailureError() {
        mView.hideLoader()
        mView.showRecoverPasswordResponse(activity.resources.getString(R.string.snkDefaultApiError))
    }

}