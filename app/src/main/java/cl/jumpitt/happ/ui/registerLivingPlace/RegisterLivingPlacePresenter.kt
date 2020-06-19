package cl.jumpitt.happ.ui.registerLivingPlace

import android.app.Activity
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.response.ErrorResponse
import cl.jumpitt.happ.network.response.RegionsResponse
import cl.jumpitt.happ.utils.parseErrJsonResponse
import cl.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response

class RegisterLivingPlacePresenter constructor(private val activity: Activity): RegisterLivingPlaceContract.Presenter, RegisterLivingPlaceContract.InteractorOutputs{
    private var mInteractor: RegisterLivingPlaceContract.Interactor = RegisterLivingPlaceInteractor()
    private var mView: RegisterLivingPlaceContract.View = activity as RegisterLivingPlaceContract.View
    private var mRouter: RegisterLivingPlaceContract.Router = RegisterLivingPlaceRouter(activity)

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
        mRouter.navigateRegisterWorkplace()
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
        mView.showLivingPlaceError(messageError)
    }

    override fun getRegionsFailureError() {
        mView.showLivingPlaceError(activity.resources.getString(R.string.snkDefaultApiError))
    }


}