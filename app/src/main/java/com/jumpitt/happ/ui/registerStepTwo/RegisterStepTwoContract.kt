package com.jumpitt.happ.ui.registerStepTwo

import com.jumpitt.happ.network.request.RegisterRequest

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