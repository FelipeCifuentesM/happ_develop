package cl.jumpitt.happ.ui.registerStepTwo

import cl.jumpitt.happ.network.request.RegisterRequest

interface RegisterStepTwoContract {

    interface View{
        fun showInitializeView()

    }

    interface Presenter{
        fun initializeView()
        fun navigateRegisterLivingPlace(registerDataObject: RegisterRequest)
    }

    interface Interactor{
        fun saveRegisterData(registerDataObject: RegisterRequest)
    }

    interface Router{
        fun navigateRegisterLivingPlace()
    }

    interface InteractorOutputs{

    }

}