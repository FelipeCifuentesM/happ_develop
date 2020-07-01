package com.jumpitt.happ.ui.changePassword

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.ChangePasswordRequest
import com.jumpitt.happ.network.response.ChangePasswordResponse
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.realm.RegisterData
import com.orhanobut.hawk.Hawk
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordInteractor(private val mIOutput: ChangePasswordContract.InteractorOutputs): ChangePasswordContract.Interactor{

    override fun putUpdatePassword(accessToken: String, changePasswordRequest: ChangePasswordRequest) {
        RestClient.instance.putChangePassword(accessToken, changePasswordRequest).
        enqueue(object: Callback<ChangePasswordResponse> {
            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                mIOutput.getRecoverPassFailureError()
            }

            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                val responseData = response.body()
                val responseCode = response.code()

                when (responseCode) {
                    200 -> {
                        responseData?.let {
                            mIOutput.getRecoverPassOutput(responseData)
                        }?: run {
                            mIOutput.getRecoverPassOutputError(responseCode, response)
                        }
                    }
                    else -> {
                        mIOutput.getRecoverPassOutputError(responseCode, response)
                    }
                }
            }
        })

    }

    override fun getAccessTokenProfile(changePasswordRequest: ChangePasswordRequest){
//        var accessToken = Hawk.get<RegisterResponse>("userProfileData").accessToken

        val realm = Realm.getDefaultInstance()
        var accessToken = realm.where(RegisterData::class.java).findFirst()?.accessToken

        if(accessToken.isNullOrBlank())
            accessToken = ""
        mIOutput.getAccessTokenProfileOutput(accessToken, changePasswordRequest)
    }


}