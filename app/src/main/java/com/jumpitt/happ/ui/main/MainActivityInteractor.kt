package com.jumpitt.happ.ui.main

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.response.NotificationHistoryResponse
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.realm.TriageReturnValue
import com.jumpitt.happ.utils.ConstantsApi
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityInteractor(private val mIOutput: MainActivityContract.InteractorOutputs): MainActivityContract.Interactor {

    override fun getAccessToken() {
        val realm = Realm.getDefaultInstance()
        var accessToken = realm.where(RegisterData::class.java).findFirst()?.accessToken

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

    override fun saveHealthCareStatus(healthCareStatusRealm: TriageReturnValue) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(TriageReturnValue::class.java)
        realm.insertOrUpdate(healthCareStatusRealm)
        realm.commitTransaction()
        realm.close()

    }

}