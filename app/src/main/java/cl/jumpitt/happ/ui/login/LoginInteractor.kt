package cl.jumpitt.happ.ui.login

import android.util.Log
import cl.jumpitt.happ.network.RestClient
import cl.jumpitt.happ.network.request.LoginAccessTokenRequest
import cl.jumpitt.happ.network.response.LoginAccessTokenResponse
import cl.jumpitt.happ.network.response.ProfileResponse
import cl.jumpitt.happ.network.response.RegisterResponse
import cl.jumpitt.happ.utils.ConstantsApi
import com.orhanobut.hawk.Hawk
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
                            interactorOutput.getProfileOutput(dataLoginResponse, dataResponseToken.accessToken)
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

    override fun saveRegisterProfile(dataLoginResponse: ProfileResponse, accessToken: String) {
        val dataProfile = RegisterResponse(profile = dataLoginResponse, accessToken = accessToken)
        Hawk.put("userProfileData", dataProfile)
    }

}
