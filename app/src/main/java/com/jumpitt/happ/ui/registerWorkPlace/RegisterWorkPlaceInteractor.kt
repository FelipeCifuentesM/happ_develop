package com.jumpitt.happ.ui.registerWorkPlace

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.response.RegionsResponse
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterWorkPlaceInteractor : RegisterWorkPlaceContract.Interactor{
    override fun getRegions(interactorOutputs: RegisterWorkPlaceContract.InteractorOutputs) {
        RestClient.instance.getRegions().
        enqueue(object: Callback<RegionsResponse> {
            override fun onFailure(call: Call<RegionsResponse>, t: Throwable) {
                interactorOutputs.getRegionsFailureError()
            }

            override fun onResponse(call: Call<RegionsResponse>, response: Response<RegionsResponse>) {
                val responseData = response.body()?.regions
                val responseCode = response.code()

                when (responseCode) {
                    200 -> {
                        responseData?.let {
                            interactorOutputs.getRegionsOutput(responseData)
                        }?: run {
                            interactorOutputs.getRegionsOutputError(responseCode, response)
                        }
                    }
                    else -> {
                        interactorOutputs.getRegionsOutputError(responseCode, response)
                    }
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