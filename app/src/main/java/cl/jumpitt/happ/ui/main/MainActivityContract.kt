package cl.jumpitt.happ.ui.main

import cl.jumpitt.happ.network.response.TriageAnswerResponse

interface MainActivityContract {
    interface View{
        fun loadFragmentMyRisk(healthCareStatus: TriageAnswerResponse)
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
        fun getHealthCareOutputError()
    }
}
