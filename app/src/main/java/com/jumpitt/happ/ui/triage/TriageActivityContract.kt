package com.jumpitt.happ.ui.triage

import com.jumpitt.happ.model.Question
import com.jumpitt.happ.network.response.PingActiveUserResponse
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.realm.TriageReturnValue
import retrofit2.Response

interface TriageActivityContract {

    interface View{
        fun showTriageAnswerError(triageAnswerError: String)
        fun showSkeleton()
        fun hideSkeleton()
        fun showLoader()
        fun hideLoader()
    }

    interface Presenter{
        fun onNextQuestionPressed(responses: List<String>, tracing: Boolean)
        fun onResultButtonPressed()
        fun getToken()
    }

    interface Interactor{
        fun getToken()
        fun getTriage(accessToken: String)
        fun nextQuestionRequested(responses: List<String>? = null, tracing: Boolean)
        fun sendTriageAnswers(tracing: Boolean, accessToken: String)
        fun getAccessTokenProfile(tracing: Boolean)
        fun getAccessTokenPing(tracing: Boolean, responseTriageAnswer: TriageAnswerResponse)
        fun getPingUserActive(accessToken: String, tracing: Boolean, responseTriageAnswer: TriageAnswerResponse)
        fun saveResult(healthCareStatusRealm: TriageReturnValue)
    }

    interface Router{
        fun displayQuestion(question: Question)
        fun displayResultScore(tracing: Boolean)
        fun displayResultDefault(tracing: Boolean)
        fun showMain()
    }

    interface InteractorOutputs{
        fun getTokenOutput(accessToken: String)
        fun onTriageLoaded()
        fun nextQuestion(question: Question?, tracing: Boolean) //if null -> send answer
        fun getTriageAnswerOutput(tracing: Boolean, responseTriageAnswer: TriageAnswerResponse)
        fun getTriageAnswerOutputError(errorCode: Int, response: Response<TriageAnswerResponse>)
        fun getAccessTokenProfileOutput(tracing: Boolean, accessToken: String)
        fun getAccessTokenPingOutput(accessToken: String, tracing: Boolean, responseTriageAnswer: TriageAnswerResponse)
        fun getTriageAnswerFailureError()
        fun getPingUserActiveOutput(dataPingResponse: PingActiveUserResponse, tracing: Boolean, responseTriageAnswer: TriageAnswerResponse)
        fun getPingUserActiveFailureError(tracing: Boolean, responseTriageAnswer: TriageAnswerResponse)
        fun getPingUserActiveOutputError(tracing: Boolean, responseTriageAnswer: TriageAnswerResponse)

    }
}