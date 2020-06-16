package cl.jumpitt.happ.ui.registerStepOne

import android.app.Activity
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.request.ValidateDNIRequest

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

    override fun postValidateDNIOutputError() {
        mView.showValidateDNIError()
    }

}