package com.jumpitt.happ.ui.registerStepTwo

import com.jumpitt.happ.network.request.RegisterRequest

interface RegisterStepTwoContract {

    interface View{
        fun showInitializeView()
        fun enabledButton()
        fun showRegisterError(messageError: String)
    }

    interface Presenter{
        fun initializeView()
        fun navigateRegisterLivingPlace(registerDataObject: RegisterRequest)
    }

    interface Interactor{
        fun getRegisterData(registerDataObject: RegisterRequest)
        fun saveRegisterData(registerData: RegisterRequest, registerDataObject: RegisterRequest)
    }

    interface Router{
        fun navigateRegisterLivingPlace()
    }

    interface InteractorOutputs{
        fun getRegisterDataOutput(registerData: RegisterRequest?, registerDataObject: RegisterRequest)
    }

}