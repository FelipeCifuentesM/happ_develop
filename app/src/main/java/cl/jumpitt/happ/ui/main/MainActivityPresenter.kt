package cl.jumpitt.happ.ui.main

import android.app.Activity
import cl.jumpitt.happ.network.response.TriageAnswerResponse

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

    override fun getHealthCareOutputError() {

    }

}