package com.jumpitt.happ.ui.notifications

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.response.NotificationHistoryResponse
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.ui.profile.ProfileFragmentContract
import com.jumpitt.happ.utils.ConstantsApi
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationInteractor(private val mIOutput: NotificationContract.InteractorOutputs): NotificationContract.Interactor {

    override fun getAccessToken() {
        val realm = Realm.getDefaultInstance()
        var accessToken = realm.where(RegisterData::class.java).findFirst()?.accessToken

        if(accessToken.isNullOrBlank())
            accessToken = ""
        mIOutput.getAccesTokenOutput(accessToken)
    }

    override fun getNotificationHistory(accessToken: String) {
        RestClient.instance.getNotificationHistory("${ConstantsApi.BEARER} $accessToken").
        enqueue(object: Callback<NotificationHistoryResponse> {
            override fun onFailure(call: Call<NotificationHistoryResponse>, t: Throwable) {
                mIOutput.getNotificationFailureError()
            }

            override fun onResponse(x: Call<NotificationHistoryResponse>, response: Response<NotificationHistoryResponse>) {
                val responseCode = response.code()
                val responseNotificationHistory = response.body()

                mIOutput.getNotificationOutput(responseNotificationHistory)

                when (responseCode) {
                    200 -> {
                        responseNotificationHistory?.let { _responseNotificationHistory ->
                            mIOutput.getNotificationOutput(_responseNotificationHistory)
                        }?: run {
                            mIOutput.getNotificationOutputError(responseCode, response)
                        }
                    }
                    else -> {
                        mIOutput.getNotificationOutputError(responseCode, response)
                    }
                }
            }

        })
    }

}