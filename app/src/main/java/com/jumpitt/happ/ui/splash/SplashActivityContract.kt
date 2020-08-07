package com.jumpitt.happ.ui.splash

import com.jumpitt.happ.realm.RegisterData

interface SplashActivityContract {
    interface View{
        fun showSplashError(messageError: String)
    }

    interface Presenter{
        fun getUserProfileData(requestPermissions: Boolean)
        fun getUserProfileDataOutput(userData: RegisterData?, requestPermissions: Boolean)
        fun validateBluetoothState()
    }

    interface Interactor{
        fun getUserProfileData(requestPermissions: Boolean)

    }

    interface Router{
        fun navigateMainActivity()
        fun navigateOnBoard()
        fun navigatePermissionBluetooth()
    }

    interface InteractorOutputs{
        fun getUserProfileDataOutput(userData: RegisterData?, requestPermissions: Boolean)
    }
}