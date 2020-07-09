package com.jumpitt.happ.ui.main

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.realm.TriageReturnValue
import retrofit2.Response

interface MainActivityContract {
    interface View{
        fun loadFragmentMyRisk(healthCareStatus: TriageAnswerResponse)
        fun showTriageAnswerError(messageError: String)
        fun showSkeleton()
        fun hideSkeleton()
    }

    interface Presenter{
        fun validatePressingDifferent(bottomNavigation: BottomNavigationView, itemId: Int): Boolean
        fun getAccessToken()
        fun validateBluetoothState()
    }

    interface Interactor{
        fun getAccessToken()
        fun getHealthCare(accessToken: String)
        fun saveHealthCareStatus(healthCareStatus: TriageReturnValue)
    }

    interface Router{
        fun navigateBluetoothPermission()
    }

    interface InteractorOutputs{
        fun getAccesTokenOutput(accessToken: String)
        fun getHealthCareOutput(healthCareStatus: TriageAnswerResponse)
        fun getHealthCareOutputError(errorCode: Int, response: Response<TriageAnswerResponse>)
        fun getHealthCareFailureError()
    }
}
