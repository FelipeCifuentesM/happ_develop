package com.jumpitt.happ.ui.login

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.LoginAccessTokenRequest
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.network.response.LoginAccessTokenResponse
import com.jumpitt.happ.network.response.PingActiveUserResponse
import com.jumpitt.happ.network.response.ProfileResponse
import com.jumpitt.happ.network.response.TokenFCMResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.realm.TraceProximityNotification
import com.jumpitt.happ.realm.TriageReturnValue
import com.jumpitt.happ.utils.ConstantsApi
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginInteractor: LoginContract.Interactor {

    override fun postLoginAccessToken(loginRequest: LoginAccessTokenRequest, interactorOutput: LoginContract.InteractorOutputs) {
        loginRequest.grantType = "password"
        loginRequest.clientId = ConstantsApi.CLIENT_ID
        loginRequest.clientSecret = ConstantsApi.CLIENT_SECRET
        RestClient.instance.postLoginAccessToken(loginRequest).
        enqueue(object: Callback<LoginAccessTokenResponse> {
            override fun onFailure(call: Call<LoginAccessTokenResponse>, t: Throwable) {
                interactorOutput.loginFailureError()
            }

            override fun onResponse(call: Call<LoginAccessTokenResponse>, response: Response<LoginAccessTokenResponse>) {
                val dataResponseToken = response.body()
                when (response.code()) {
                    200 -> {
                        dataResponseToken?.let {
                            interactorOutput.postLoginAccessTokenOutput(dataResponseToken)
                        }?: run {
                            interactorOutput.postLoginAccessTokenOutputError(response.code(), response)
                        }
                    }
                    else -> {
                        interactorOutput.postLoginAccessTokenOutputError(response.code(), response)
                    }
                }
            }
        })
    }

    override fun getProfile(dataResponseToken: LoginAccessTokenResponse, interactorOutput: LoginContract.InteractorOutputs){
        RestClient.instance.getProfile( "${ConstantsApi.BEARER} ${dataResponseToken.accessToken}").
        enqueue(object: Callback<ProfileResponse>{
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                interactorOutput.loginFailureError()
            }

            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                val responseData = response.body()
                val responseCode = response.code()

                when (responseCode) {
                    200 -> {
                        responseData?.let {dataLoginResponse ->
                            interactorOutput.getProfileOutput(dataLoginResponse, dataResponseToken.accessToken, dataResponseToken.refreshToken)
                        }?: run {
                            interactorOutput.getProfileOutputError(responseCode, response)
                        }
                    }
                    else -> {
                        interactorOutput.getProfileOutputError(responseCode, response)
                    }
                }
            }

        })
    }

    override fun saveRegisterProfile(userRealm: RegisterData) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(RegisterData::class.java)
        realm.insertOrUpdate(userRealm)
        realm.commitTransaction()
        realm.close()
    }

    override fun postRegisterTokenFCM(accessToken: String, tokenFCMRequest: TokenFCMRequest, interactorOutput: LoginContract.InteractorOutputs) {
        RestClient.instance.postRegisterTokenFCM("${ConstantsApi.BEARER} $accessToken", tokenFCMRequest).
        enqueue(object: Callback<TokenFCMResponse> {
            override fun onFailure(call: Call<TokenFCMResponse>, t: Throwable) {
                interactorOutput.postRegisterTokenFCMFailureError(accessToken)
            }

            override fun onResponse(call: Call<TokenFCMResponse>, response: Response<TokenFCMResponse>) {
                interactorOutput.postRegisterTokenFCMOutput(accessToken)
            }
        })
    }

    override fun getPingUserActive(accessToken: String, interactorOutput: LoginContract.InteractorOutputs){
        RestClient.instanceTracing.getPingUserActive("${ConstantsApi.BEARER} $accessToken").
        enqueue(object: Callback<PingActiveUserResponse>{
            override fun onFailure(call: Call<PingActiveUserResponse>, t: Throwable) {
                val dataPingResponseNull = PingActiveUserResponse(null)
                interactorOutput.getPingUserActiveFailureError(dataPingResponseNull)
            }

            override fun onResponse(call: Call<PingActiveUserResponse>, response: Response<PingActiveUserResponse>) {
                val responseData = response.body()
                val responseCode = response.code()

                when (responseCode) {
                    200 -> {
                        responseData?.let {dataPingResponse ->
                            interactorOutput.getPingUserActiveOutput(dataPingResponse)
                        }?: run {
                            val dataPingResponseNull = PingActiveUserResponse(null)
                            interactorOutput.getPingUserActiveOutputError(dataPingResponseNull, responseCode)
                        }
                    }
                    else -> {
                        val dataPingResponseNull = PingActiveUserResponse(null)
                        interactorOutput.getPingUserActiveOutputError(dataPingResponseNull, responseCode)
                    }
                }
            }

        })
    }

    override fun getAccessToken(interactorOutput: LoginContract.InteractorOutputs) {
        val realm = Realm.getDefaultInstance()
        var accessToken = realm.where(RegisterData::class.java).findFirst()?.accessToken

        if(accessToken.isNullOrBlank())
            accessToken = ""
        interactorOutput.getAccessTokenOutput(accessToken)
    }

    override fun deleteProfileData() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(RegisterData::class.java)
        realm.delete(TriageReturnValue::class.java)
        realm.delete(TraceProximityNotification::class.java)
        realm.commitTransaction()
        realm.close()
    }

}
