package cl.jumpitt.happ.ui.registerWorkPlace

import cl.jumpitt.happ.network.RestClient
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.response.RegionsResponse
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterWorkPlaceInteractor : RegisterWorkPlaceContract.Interactor{
    override fun getRegions(interactorOutputs: RegisterWorkPlaceContract.InteractorOutputs) {
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
        registerData.workCommuneId = registerDataObject.workCommuneId
        Hawk.put("registerData", registerData)
    }
}