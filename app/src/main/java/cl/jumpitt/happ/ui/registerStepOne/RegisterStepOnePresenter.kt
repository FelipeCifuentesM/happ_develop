package cl.jumpitt.happ.ui.registerStepOne

import android.app.Activity
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.request.ValidateDNIRequest
import cl.jumpitt.happ.network.response.ErrorResponse
import cl.jumpitt.happ.network.response.ValidateDNIResponse
import cl.jumpitt.happ.utils.parseErrJsonResponse
import cl.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response

class RegisterStepOnePresenter constructor(private val activity: Activity): RegisterStepOneContract.Presenter, RegisterStepOneContract.InteractorOutputs {
    private var mInteractor: RegisterStepOneContract.Interactor = RegisterStepOneInteractor()
    private var mView: RegisterStepOneContract.View = activity as RegisterStepOneContract.View
    private var mRouter: RegisterStepOneContract.Router = RegisterStepOneRouter(activity)

    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun postValidateDNI(validateDNIRequest: ValidateDNIRequest) {
        mInteractor.postValidateDNI(validateDNIRequest, this)
    }

    override fun postValidateDNIOutput(validateDNIRequest: ValidateDNIRequest) {
        val registerDataObject = RegisterRequest(dni= validateDNIRequest.dni)
        mInteractor.saveRegisterData(registerDataObject)
        mRouter.navigateRegisterStepTwo()
    }

    override fun postValidateDNIOutputError(errorCode: Int, response: Response<ValidateDNIResponse>) {
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showValidateDNIError(messageError)
    }

    override fun postValidateDNIFailureError() {
        mView.showValidateDNIError(activity.resources.getString(R.string.snkDefaultApiError))
    }

}