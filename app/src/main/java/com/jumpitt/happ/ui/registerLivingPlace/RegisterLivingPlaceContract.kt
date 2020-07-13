package com.jumpitt.happ.ui.registerLivingPlace

import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.response.RegionsResponse
import retrofit2.Response

interface RegisterLivingPlaceContract {

    interface View{
        fun showInitializeView()
        fun loadRegions(regionsNameList: ArrayList<String>, regionsList: List<RegionsResponse.DataRegions>)
        fun loadCommunes(communesNameList: ArrayList<String>)
        fun showLivingPlaceError(messageError: String)

    }

    interface Presenter{
        fun initializeView()
        fun getRegions()
        fun loadCommunes(communesList: List<RegionsResponse.DataRegions.Communes>)
        fun navigateRegisterWorkplace(registerDataObject: RegisterRequest)
    }

    interface Interactor{
        fun getRegions(interactorOutputs: InteractorOutputs)
        fun getRegisterData(registerDataObject: RegisterRequest)
        fun saveRegisterData(registerData: RegisterRequest, registerDataObject: RegisterRequest)
    }

    interface Router{
        fun navigateRegisterWorkplace()
    }

    interface InteractorOutputs{
        fun getRegionsOutput(regionsList: List<RegionsResponse.DataRegions>)
        fun getRegionsOutputError(errorCode: Int, response: Response<RegionsResponse>)
        fun getRegisterDataOutputs(registerData:RegisterRequest?, registerDataObject:RegisterRequest)
        fun getRegionsFailureError()
    }
}