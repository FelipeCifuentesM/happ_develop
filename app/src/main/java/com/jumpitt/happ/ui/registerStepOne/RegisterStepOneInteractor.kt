package com.jumpitt.happ.ui.registerStepOne

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.request.ValidateDNIRequest
import com.jumpitt.happ.network.response.ValidateDNIResponse
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterStepOneInteractor: RegisterStepOneContract.Interactor{

    override fun postValidateDNI(validateDNIRequest: ValidateDNIRequest, interactorOutputs: RegisterStepOneContract.InteractorOutputs) {
        RestClient.instance.postValidateDNI(validateDNIRequest).
        enqueue(object: Callback<ValidateDNIResponse> {
            override fun onFailure(call: Call<ValidateDNIResponse>, t: Throwable) {
                interactorOutputs.postValidateDNIFailureError()
            }

            override fun onResponse(call: Call<ValidateDNIResponse>,response: Response<ValidateDNIResponse>) {
                val responseCode = response.code()
                val responseData = response.body()

                when (responseCode) {
                    200 -> {
                        responseData?.let {
                            interactorOutputs.postValidateDNIOutput(validateDNIRequest)
                        }?: run {
                            interactorOutputs.postValidateDNIOutputError(responseCode, response)
                        }
                    }
                    else -> {
                        interactorOutputs.postValidateDNIOutputError(responseCode, response)
                    }
                }
            }
        })
    }

    override fun saveRegisterData(registerDataObject: RegisterRequest) {
        Hawk.put("registerData", registerDataObject)
    }

}