package com.jumpitt.happ.ui.main

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.utils.ConstantsApi
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityInteractor(private val mIOutput: MainActivityContract.InteractorOutputs): MainActivityContract.Interactor {

    override fun getAccessToken() {
        var accessToken = Hawk.get<RegisterResponse>("userProfileData").accessToken
        if(accessToken.isNullOrBlank())
            accessToken = ""
        mIOutput.getAccesTokenOutput(accessToken)
    }

    override fun getHealthCare(accessToken: String) {
        RestClient.instance.getHealthCare("${ConstantsApi.BEARER} $accessToken").
        enqueue(object: Callback<TriageAnswerResponse> {
            override fun onFailure(call: Call<TriageAnswerResponse>, t: Throwable) {
                mIOutput.getHealthCareFailureError()
            }

            override fun onResponse(call: Call<TriageAnswerResponse>, response: Response<TriageAnswerResponse>) {
                val responseCode = response.code()
                val responseData = response.body()

                when (responseCode) {
                    200 -> {
                        responseData?.let { healthCareStatus ->
                            mIOutput.getHealthCareOutput(healthCareStatus)
                        }?: run {
                            mIOutput.getHealthCareOutputError(responseCode, response)
                        }
                    }
                    else -> {
                        mIOutput.getHealthCareOutputError(responseCode, response)
                    }
                }
            }
        })
    }

    override fun saveHealthCareStatus(healthCareStatus: TriageAnswerResponse) {
        val healthCareStatusLocal = Hawk.get<TriageAnswerResponse>("triageReturnValue")
        healthCareStatusLocal?.let {
            Hawk.delete("triageReturnValue")
        }
        Hawk.put("triageReturnValue", healthCareStatus)
    }

}