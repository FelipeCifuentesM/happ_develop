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
    }

    interface Interactor{
        fun getUserProfileData()
    }

    interface Router{
        fun navigateTracingLog()
    }

    interface InteractorOutputs{
        fun getUserProfileDataOutput(userData: RegisterResponse)
    }
}