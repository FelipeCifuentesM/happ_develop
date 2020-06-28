package com.jumpitt.happ.ui.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jumpitt.happ.network.response.RegisterResponse

interface ProfileFragmentContract {
    interface View{
        fun showInitializeView()
        fun showUnwrappingValues(userData: RegisterResponse)
        fun showSnackBar(message: String)
    }

    interface Presenter{
        fun initializeView()
        fun getUserProfileData()
        fun navigateTracingLog()
        fun deleteProfileData()
        fun navigateChangePassword(fragment: ProfileFragment, activity: FragmentActivity?)
    }

    interface Interactor{
        fun getUserProfileData()
        fun deleteProfileData()
    }

    interface Router{
        fun navigateTracingLog()
        fun navigateLogin()
        fun navigateChangePassword(fragment: ProfileFragment, activity: FragmentActivity?)
    }

    interface InteractorOutputs{
        fun getUserProfileDataOutput(userData: RegisterResponse)
    }
}