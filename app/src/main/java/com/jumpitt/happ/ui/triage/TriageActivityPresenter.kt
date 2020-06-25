package com.jumpitt.happ.ui.triage

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.jumpitt.happ.R
import com.jumpitt.happ.model.Question
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response

class TriageActivityPresenter constructor(private val activity: Activity): TriageActivityContract.Presenter,
    TriageActivityContract.InteractorOutputs {
    private var mInteractor: TriageActivityContract.Interactor =
        TriageActivityInteractor(this)
    private var mView: TriageActivityContract.View = activity as TriageActivityContract.View
    private var mRouter: TriageActivityContract.Router =
        TriageActivityRouter(mView as AppCompatActivity)


    override fun onNextQuestionPressed(responses: List<String>, tracing: Boolean) {
        mInteractor.nextQuestionRequested(responses, tracing)
    }

    override fun onResultButtonPressed() {
        mRouter.showMain()
    }

    override fun getToken() {
        mInteractor.getToken()
    }

    override fun getTokenOutput(accessToken: String) {
        mInteractor.getTriage(accessToken)
    }

    override fun onTriageLoaded() {
        mInteractor.nextQuestionRequested(tracing = false)
    }

    override fun nextQuestion(question: Question?, tracing: Boolean) {
        question?.let {
            mRouter.displayQuestion(it)
            return
        }
        mInteractor.getAccessTokenProfile(tracing)
    }

    override fun getAccessTokenProfileOutput(tracing: Boolean, accessToken: String) {
        mInteractor.sendTriageAnswers(tracing, accessToken)
    }

    override fun getTriageAnswerOutput(tracing: Boolean, responseTriageAnswer: TriageAnswerResponse) {
            mInteractor.saveResult(responseTriageAnswer)
            mRouter.displayResult(tracing)
    }

    override fun getTriageAnswerOutputError(errorCode: Int, response: Response<TriageAnswerResponse>) {
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showTriageAnswerError(messageError)
    }

    override fun getTriageAnswerFailureError() {
        mView.showTriageAnswerError(activity.resources.getString(R.string.snkDefaultApiError))
    }

}