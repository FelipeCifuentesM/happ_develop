package com.jumpitt.happ.ui.login

import android.util.Log
import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.LoginAccessTokenRequest
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.network.response.LoginAccessTokenResponse
import com.jumpitt.happ.network.response.ProfileResponse
import com.jumpitt.happ.network.response.TokenFCMResponse
import com.jumpitt.happ.network.response.ValidateDNIResponse
import com.jumpitt.happ.realm.RegisterData
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
                interactorOutput.LoginFailureError()
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
                interactorOutput.LoginFailureError()
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
        Log.e("Borrar", "token diospositivo: "+tokenFCMRequest.token)
        RestClient.instance.postRegisterTokenFCM(accessToken, tokenFCMRequest).
        enqueue(object: Callback<TokenFCMResponse> {
            override fun onFailure(call: Call<TokenFCMResponse>, t: Throwable) {
                interactorOutput.postRegisterTokenFCMFailureError()
            }

            override fun onResponse(call: Call<TokenFCMResponse>, response: Response<TokenFCMResponse>) {
                val responseCode = response.code()
                val responseData = response.body()

                interactorOutput.postRegisterTokenFCMOutput()

            }
        })
    }

}
