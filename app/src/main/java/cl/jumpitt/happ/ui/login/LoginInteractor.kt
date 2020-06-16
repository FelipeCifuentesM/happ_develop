package cl.jumpitt.happ.ui.login

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

            }

            override fun onResponse(call: Call<LoginAccessTokenResponse>, response: Response<LoginAccessTokenResponse>) {
                val dataResponseToken = response.body()
                val dataResponseCode = response.code()
                dataResponseToken?.let {
                    if(dataResponseCode == 200){
                        interactorOutput.postLoginAccessTokenOutput(dataResponseToken)
                    }else{
                        interactorOutput.postLoginAccessTokenOutputError()
                    }
                }?: run {
                    interactorOutput.postLoginAccessTokenOutputError()
                }
            }
        })
    }

    override fun getProfile(dataResponseToken: LoginAccessTokenResponse, interactorOutput: LoginContract.InteractorOutputs){
        RestClient.instance.getProfile( "${ConstantsApi.BEARER} ${dataResponseToken.accessToken}").
        enqueue(object: Callback<ProfileResponse>{
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                interactorOutput.getProfileOutputError()
            }

            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                val dataResponse = response.body()
                val dataResponseCode = response.code()
                if(dataResponseCode == 200){
                    dataResponse?.let {dataLoginResponse ->
                        interactorOutput.getProfileOutput(dataLoginResponse, dataResponseToken.accessToken)
                    }?: run {
                        interactorOutput.getProfileOutputError()
                    }
                }else{
                    interactorOutput.getProfileOutputError()
                }
            }

        })
    }

    override fun saveRegisterProfile(dataLoginResponse: ProfileResponse, accessToken: String) {
        val dataProfile = RegisterResponse(profile = dataLoginResponse, accessToken = accessToken)
        Hawk.put("userProfileData", dataProfile)
    }

}
