package cl.jumpitt.happ.ui.registerStepOne

import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.request.ValidateDNIRequest

interface RegisterStepOneContract {
    interface View{
        fun showInitializeView()
        fun showValidateDNIError()
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
        fun postValidateDNIOutputError()
    }
}