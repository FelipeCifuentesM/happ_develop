package cl.jumpitt.happ.ui.triage

import cl.jumpitt.happ.model.Question
import cl.jumpitt.happ.network.response.TriageAnswerResponse

interface TriageActivityContract {

    interface View{
        fun showTriageAnswerError(triageAnswerError: String)
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
        fun saveResult(responseTriageAnswer: TriageAnswerResponse)
    }

    interface Router{
        fun displayQuestion(question: Question)
        fun displayResult(tracing: Boolean)
        fun showMain()
    }

    interface InteractorOutputs{
        fun getTokenOutput(accessToken: String)
        fun onTriageLoaded()
        fun nextQuestion(question: Question?, tracing: Boolean) //if null -> send answer
        fun getTriageAnswerOutput(tracing: Boolean, responseTriageAnswer: TriageAnswerResponse)
        fun getTriageAnswerOutputError(errorCode: Int)
        fun getAccessTokenProfileOutput(tracing: Boolean, accessToken: String)
    }
}