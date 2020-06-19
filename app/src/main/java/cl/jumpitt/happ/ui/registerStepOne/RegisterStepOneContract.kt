package cl.jumpitt.happ.ui.registerStepOne

import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.request.ValidateDNIRequest
import cl.jumpitt.happ.network.response.ValidateDNIResponse
import retrofit2.Response

interface RegisterStepOneContract {
    interface View{
        fun showInitializeView()
        fun showValidateDNIError(messageError: String)
    }

    interface Presenter{
        fun initializeView()
        fun postValidateDNI(validateDNIRequest: ValidateDNIRequest)
    }

    interface Interactor{
        fun postValidateDNI(validateDNIRequest: ValidateDNIRequest, interactorOutputs: InteractorOutputs)
        fun saveRegisterData(registerDataObject: RegisterRequest)
    }

    interface Router{
        fun navigateRegisterStepTwo()
    }

    interface InteractorOutputs{
        fun postValidateDNIOutput(validateDNIRequest: ValidateDNIRequest)
        fun postValidateDNIOutputError(errorCode: Int, response: Response<ValidateDNIResponse>)
        fun postValidateDNIFailureError()
    }
}