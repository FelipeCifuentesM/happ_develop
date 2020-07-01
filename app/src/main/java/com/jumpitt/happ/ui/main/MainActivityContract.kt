package com.jumpitt.happ.ui.main

import com.jumpitt.happ.network.response.TriageAnswerResponse
import retrofit2.Response

interface MainActivityContract {
    interface View{
        fun loadFragmentMyRisk(healthCareStatus: TriageAnswerResponse)
        fun showTriageAnswerError(messageError: String)
//        fun showSkeleton()
//        fun hideSkeleton()
    }

    interface Presenter{
        fun getAccessToken()
    }

    interface Interactor{
        fun getAccessToken()
        fun getHealthCare(accessToken: String)
        fun saveHealthCareStatus(healthCareStatus: TriageAnswerResponse)
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
