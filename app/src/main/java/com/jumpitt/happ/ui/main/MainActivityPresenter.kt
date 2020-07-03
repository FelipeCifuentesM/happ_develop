package com.jumpitt.happ.ui.main

import android.app.Activity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.realm.TriageReturnValue
import com.jumpitt.happ.utils.qualifyResponseErrorDefault
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivityPresenter constructor(private val activity: Activity): MainActivityContract.Presenter, MainActivityContract.InteractorOutputs {
    private var mInteractor: MainActivityContract.Interactor = MainActivityInteractor(this)
    private var mView: MainActivityContract.View = activity as MainActivityContract.View
    private var mRouter: MainActivityContract.Router = MainActivityRouter(activity)

    override fun validatePressingDifferent(bottomNavigation: BottomNavigationView, itemId: Int): Boolean {
        return bottomNavigation.menu.findItem(itemId).isChecked
    }

    override fun getAccessToken() {
        mView.showSkeleton()
        mInteractor.getAccessToken()
    }

    override fun getAccesTokenOutput(accessToken: String) {
        mInteractor.getHealthCare(accessToken)
    }

    override fun getHealthCareOutput(healthCareStatus: TriageAnswerResponse) {
        val healthCareStatusRealm = TriageReturnValue(healthCareStatus.score, healthCareStatus.risk?.title, healthCareStatus.risk?.level, healthCareStatus.risk?.description,
            healthCareStatus.risk?.message, healthCareStatus.latestReview, healthCareStatus.passport?.timeRemainingVerbose, healthCareStatus.passport?.validationUrl)
        mInteractor.saveHealthCareStatus(healthCareStatusRealm)
//        mView.hideSkeleton()
        mView.loadFragmentMyRisk(healthCareStatus)

    }

    override fun getHealthCareOutputError(errorCode: Int, response: Response<TriageAnswerResponse>) {
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.hideSkeleton()
        mView.showTriageAnswerError(messageError)
    }

    override fun getHealthCareFailureError() {
        mView.hideSkeleton()
        mView.showTriageAnswerError(activity.resources.getString(R.string.snkDefaultApiError))
    }

}