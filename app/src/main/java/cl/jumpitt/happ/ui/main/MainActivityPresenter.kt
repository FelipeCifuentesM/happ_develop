package cl.jumpitt.happ.ui.main

import android.app.Activity
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.response.TriageAnswerResponse
import cl.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response

class MainActivityPresenter constructor(private val activity: Activity): MainActivityContract.Presenter, MainActivityContract.InteractorOutputs {
    private var mInteractor: MainActivityContract.Interactor = MainActivityInteractor(this)
    private var mView: MainActivityContract.View = activity as MainActivityContract.View
    private var mRouter: MainActivityContract.Router = MainActivityRouter(activity)

    override fun getAccessToken() {
        mInteractor.getAccessToken()
    }

    override fun getAccesTokenOutput(accessToken: String) {
        mInteractor.getHealthCare(accessToken)
    }

    override fun getHealthCareOutput(healthCareStatus: TriageAnswerResponse) {
        mInteractor.saveHealthCareStatus(healthCareStatus)
        mView.loadFragmentMyRisk(healthCareStatus)

    }

    override fun getHealthCareOutputError(errorCode: Int, response: Response<TriageAnswerResponse>) {
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showTriageAnswerError(messageError)
    }

    override fun getHealthCareFailureError() {
        mView.showTriageAnswerError(activity.resources.getString(R.string.snkDefaultApiError))
    }


}