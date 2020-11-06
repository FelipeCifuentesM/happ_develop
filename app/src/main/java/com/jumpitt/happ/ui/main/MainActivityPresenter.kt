package com.jumpitt.happ.ui.main

import android.app.Activity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.realm.TriageReturnValue
import com.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response

class MainActivityPresenter constructor(private val activity: Activity): MainActivityContract.Presenter, MainActivityContract.InteractorOutputs {
    private var mInteractor: MainActivityContract.Interactor = MainActivityInteractor(this)
    private var mView: MainActivityContract.View = activity as MainActivityContract.View
    private var mRouter: MainActivityContract.Router = MainActivityRouter(activity)

    override fun loadFragmentHappHome() {
        mView.loadFragmentHappHome()
    }

    override fun validatePressingDifferent(bottomNavigation: BottomNavigationView, itemId: Int): Boolean {
        return bottomNavigation.menu.findItem(itemId).isChecked
    }

    override fun getAccessToken(isShowSkeleton: Boolean) {
        if(isShowSkeleton){
            mView.showSkeleton()
            mInteractor.getAccessToken()
        }else{
            mView.showLoader()
            mInteractor.getAccessToken()
        }
    }

    override fun getAccesTokenOutput(accessToken: String) {
        mInteractor.getHealthCare(accessToken)
    }

    override fun getHealthCareOutput(healthCareStatus: TriageAnswerResponse) {
        val healthCareStatusRealm = TriageReturnValue(healthCareStatus.score, healthCareStatus.risk?.title, healthCareStatus.risk?.level, healthCareStatus.risk?.description,
            healthCareStatus.risk?.message, healthCareStatus.latestReview, healthCareStatus.passport?.timeRemainingVerbose, healthCareStatus.passport?.validationUrl,
            healthCareStatus.text?.title, healthCareStatus.text?.description)
        mInteractor.saveHealthCareStatus(healthCareStatusRealm)
        mView.loadFragmentMyRisk(healthCareStatus)

    }

    override fun getHealthCareOutputError(errorCode: Int, response: Response<TriageAnswerResponse>) {
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.hideSkeleton()
        mView.hideLoader()
        mView.showTriageAnswerError(messageError)
        mView.loadFragmentMyRiskFailure(errorCode)
    }

    override fun getHealthCareFailureError() {
        mView.hideSkeleton()
        mView.hideLoader()
        mView.showTriageAnswerError(activity.resources.getString(R.string.snkDefaultApiError))
        mView.loadFragmentMyRiskFailure(0)
    }

}