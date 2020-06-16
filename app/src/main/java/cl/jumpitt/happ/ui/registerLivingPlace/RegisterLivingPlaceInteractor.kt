package cl.jumpitt.happ.ui.registerLivingPlace

import cl.jumpitt.happ.network.RestClient
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.response.RegionsResponse
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterLivingPlaceInteractor: RegisterLivingPlaceContract.Interactor{

    override fun getRegions(interactorOutputs: RegisterLivingPlaceContract.InteractorOutputs) {
        RestClient.instance.getRegions().
        enqueue(object: Callback<RegionsResponse> {
            override fun onFailure(call: Call<RegionsResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<RegionsResponse>, response: Response<RegionsResponse>) {
                val dataResponse = response.body()?.regions
                if (response.code() == 200 && dataResponse != null) {
                    interactorOutputs.getRegionsOutput(dataResponse)
                }else{
                    interactorOutputs.getRegionsOutputError()
                }
            }
        })
    }

    override fun saveRegisterData(registerDataObject: RegisterRequest) {
        val registerData = Hawk.get<RegisterRequest>("registerData")
        registerData.homeCommuneId = registerDataObject.homeCommuneId
        Hawk.put("registerData", registerData)
    }

}