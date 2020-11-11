package com.jumpitt.happ.ui.main

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.realm.TriageReturnValue
import retrofit2.Response

interface MainActivityContract {
    interface View{
        fun loadFragmentHappHome()
        fun loadFragmentMyRisk(healthCareStatus: TriageAnswerResponse, isButtonEnabled: Boolean = true)
        fun loadFragmentMyRiskFailure(errorCode: Int)
        fun showTriageAnswerError(messageError: String)
        fun showSkeleton()
        fun hideSkeleton()
        fun showLoader()
        fun hideLoader()
        fun setNavigationTab()
    }

    interface Presenter{
        fun loadFragment(isMyRiskTabSelected: Boolean = false)
        fun validatePressingDifferent(bottomNavigation: BottomNavigationView, itemId: Int): Boolean
        fun getAccessToken(isShowSkeleton: Boolean = true)
    }

    interface Interactor{
        fun getAccessToken()
        fun getHealthCare(accessToken: String)
        fun saveHealthCareStatus(healthCareStatus: TriageReturnValue)
    }

    interface Router{
    }

    interface InteractorOutputs{
        fun getAccesTokenOutput(accessToken: String)
        fun getHealthCareOutput(healthCareStatus: TriageAnswerResponse)
        fun getHealthCareOutputError(errorCode: Int, response: Response<TriageAnswerResponse>)
        fun getHealthCareFailureError()
    }
}
