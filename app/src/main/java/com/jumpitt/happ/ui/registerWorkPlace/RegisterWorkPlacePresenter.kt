package com.jumpitt.happ.ui.registerWorkPlace

import android.app.Activity
import com.jumpitt.happ.R
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.response.RegionsResponse
import com.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response

class RegisterWorkPlacePresenter constructor(private val activity: Activity): RegisterWorkPlaceContract.Presenter, RegisterWorkPlaceContract.InteractorOutputs{
    private var mInteractor: RegisterWorkPlaceContract.Interactor = RegisterWorkPlaceInteractor()
    private var mView: RegisterWorkPlaceContract.View = activity as RegisterWorkPlaceContract.View
    private var mRouter: RegisterWorkPlaceContract.Router = RegisterWorkPlaceRouter(activity)

    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun getRegions() {
        mInteractor.getRegions(this)
    }

    override fun loadCommunes(communesList: List<RegionsResponse.DataRegions.Communes>) {
        val communesNameList: ArrayList<String> = ArrayList()
        communesList.forEach {
            communesNameList.add(it.name)
        }
        mView.loadCommunes(communesNameList)
    }

    override fun navigateRegisterWorkplace(registerDataObject: RegisterRequest) {
        mInteractor.saveRegisterData(registerDataObject)
        mRouter.navigateRegisterPermissions()
    }

    override fun getRegionsOutput(regionsList: List<RegionsResponse.DataRegions>) {
        val regionsNameList: ArrayList<String> = ArrayList()
        regionsList.forEach {
            regionsNameList.add(it.name)
        }

        mView.loadRegions(regionsNameList, regionsList)
    }

    override fun getRegionsOutputError(errorCode: Int, response: Response<RegionsResponse>) {
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showWorkPlaceError(messageError)
    }

    override fun getRegionsFailureError() {
        mView.showWorkPlaceError(activity.resources.getString(R.string.snkDefaultApiError))
    }

}