package com.jumpitt.happ.ui.registerPermissions

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.network.response.PingActiveUserResponse
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.network.response.TokenFCMResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.utils.ConstantsApi
import com.orhanobut.hawk.Hawk
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPermissionsInteractor: RegisterPermissionsContract.Interactor{
    override fun getRegisterData(interactorOutputs: RegisterPermissionsContract.InteractorOutputs) {
        val registerData:RegisterRequest? = Hawk.get<RegisterRequest>("registerData")
        interactorOutputs.getRegisterDataOutput(registerData)
    }

    override fun postRegister(registerRequest: RegisterRequest, interactorOutputs: RegisterPermissionsContract.InteractorOutputs){
        RestClient.instance.postRegister(registerRequest).
        enqueue(object: Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                interactorOutputs.postRegisterFailureError()
            }

            override fun onResponse(x: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                val responseCode = response.code()
                val responseRegisterProfile = response.body()

                when (responseCode) {
                    200 -> {
                        responseRegisterProfile?.let {
                            interactorOutputs.postRegisterOutput(responseRegisterProfile)
                        }?: run {
                            interactorOutputs.postRegisterOutputError(responseCode, response)
                        }
                    }
                    else -> {
                        interactorOutputs.postRegisterOutputError(responseCode, response)
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

    override fun postRegisterTokenFCM(accessToken: String, tokenFCMRequest: TokenFCMRequest, interactorOutputs: RegisterPermissionsContract.InteractorOutputs){
        RestClient.instance.postRegisterTokenFCM("${ConstantsApi.BEARER} $accessToken", tokenFCMRequest).
        enqueue(object: Callback<TokenFCMResponse> {
            override fun onFailure(call: Call<TokenFCMResponse>, t: Throwable) {
                interactorOutputs.postRegisterTokenFCMFailureError(accessToken)
            }

            override fun onResponse(call: Call<TokenFCMResponse>, response: Response<TokenFCMResponse>) {
                val responseCode = response.code()
                val responseData = response.body()

                interactorOutputs.postRegisterTokenFCMOutput(accessToken)

            }
        })
    }

    override fun getAccessToken(interactorOutputs: RegisterPermissionsContract.InteractorOutputs) {
        val realm = Realm.getDefaultInstance()
        var accessToken = realm.where(RegisterData::class.java).findFirst()?.accessToken

        if(accessToken.isNullOrBlank())
            accessToken = ""
        interactorOutputs.getAccessTokenOutput(accessToken)
    }

    override fun getPingUserActive(accessToken: String, interactorOutput: RegisterPermissionsContract.InteractorOutputs){
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
                            interactorOutput.getPingUserActiveOutputError(dataPingResponseNull)
                        }
                    }
                    else -> {
                        val dataPingResponseNull = PingActiveUserResponse(null)
                        interactorOutput.getPingUserActiveOutputError(dataPingResponseNull)
                    }
                }
            }

        })
    }

}