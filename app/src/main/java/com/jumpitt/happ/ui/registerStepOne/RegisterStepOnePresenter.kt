package com.jumpitt.happ.ui.registerStepOne

import android.app.Activity
import com.jumpitt.happ.R
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.request.ValidateDNIRequest
import com.jumpitt.happ.network.response.ValidateDNIResponse
import com.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response

class RegisterStepOnePresenter constructor(private val activity: Activity): RegisterStepOneContract.Presenter, RegisterStepOneContract.InteractorOutputs {
    private var mInteractor: RegisterStepOneContract.Interactor = RegisterStepOneInteractor()
    private var mView: RegisterStepOneContract.View = activity as RegisterStepOneContract.View
    private var mRouter: RegisterStepOneContract.Router = RegisterStepOneRouter(activity)

    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun postValidateDNI(validateDNIRequest: ValidateDNIRequest) {
        mView.showLoader()
        mInteractor.postValidateDNI(validateDNIRequest, this)
    }

    override fun postValidateDNIOutput(validateDNIRequest: ValidateDNIRequest) {
        mView.hideLoader()
        val registerDataObject = RegisterRequest(dni= validateDNIRequest.dni)
        mInteractor.saveRegisterData(registerDataObject)
        mRouter.navigateRegisterStepTwo()
    }

    override fun postValidateDNIOutputError(errorCode: Int, response: Response<ValidateDNIResponse>) {
        mView.hideLoader()
        mView.enabledButton()
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showValidateDNIError(messageError)
    }

    override fun postValidateDNIFailureError() {
        mView.hideLoader()
        mView.enabledButton()
        mView.showValidateDNIError(activity.resources.getString(R.string.snkDefaultApiError))
    }

}