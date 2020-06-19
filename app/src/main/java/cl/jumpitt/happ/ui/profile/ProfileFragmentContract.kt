package cl.jumpitt.happ.ui.profile

import cl.jumpitt.happ.network.response.RegisterResponse

interface ProfileFragmentContract {
    interface View{
        fun showInitializeView()
        fun showUnwrappingValues(userData: RegisterResponse)
    }

    interface Presenter{
        fun initializeView()
        fun getUserProfileData()
        fun navigateTracingLog()
        fun deleteProfileData()
        fun navigateChangePassword()
    }

    interface Interactor{
        fun getUserProfileData()
        fun deleteProfileData()
    }

    interface Router{
        fun navigateTracingLog()
        fun navigateLogin()
        fun navigateChangePassword()
    }

    interface InteractorOutputs{
        fun getUserProfileDataOutput(userData: RegisterResponse)
    }
}