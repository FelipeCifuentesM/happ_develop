package cl.jumpitt.happ.ui.splash

import cl.jumpitt.happ.network.response.RegisterResponse

interface SplashActivityContract {
    interface View{

    }

    interface Presenter{
        fun getUserProfileData(requestPermissions: Boolean)
        fun getUserProfileDataOutput(userData: RegisterResponse?, requestPermissions: Boolean)
    }

    interface Interactor{
        fun getUserProfileData(requestPermissions: Boolean)

    }

    interface Router{
        fun navigateMainActivity()
        fun navigateOnBoard()
    }

    interface InteractorOutputs{
        fun getUserProfileDataOutput(userData: RegisterResponse?, requestPermissions: Boolean)
    }
}