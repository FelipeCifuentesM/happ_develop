package com.jumpitt.happ.ui.registerLivingPlace

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.response.RegionsResponse
import com.jumpitt.happ.ui.registerStepTwo.RegisterStepTwoContract
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterLivingPlaceInteractor(private val mIOutput: RegisterLivingPlaceContract.InteractorOutputs): RegisterLivingPlaceContract.Interactor{

    override fun getRegions(interactorOutputs: RegisterLivingPlaceContract.InteractorOutputs) {
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

    override fun getRegisterData(registerDataObject: RegisterRequest) {
        val registerData:RegisterRequest? = Hawk.get<RegisterRequest>("registerData")
        mIOutput.getRegisterDataOutputs(registerData, registerDataObject)
    }

    override fun saveRegisterData(registerData: RegisterRequest, registerDataObject: RegisterRequest) {
        registerData.homeCommuneId = registerDataObject.homeCommuneId
        Hawk.put("registerData", registerData)
    }

}