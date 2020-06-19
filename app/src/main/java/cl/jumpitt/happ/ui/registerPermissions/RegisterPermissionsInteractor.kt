package cl.jumpitt.happ.ui.registerPermissions

import cl.jumpitt.happ.network.RestClient
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.response.RegisterResponse
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPermissionsInteractor: RegisterPermissionsContract.Interactor{
    override fun getRegisterData(interactorOutputs: RegisterPermissionsContract.InteractorOutputs) {
        val registerData = Hawk.get<RegisterRequest>("registerData")
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

    override fun saveRegisterProfile(dataRegisterResponse: RegisterResponse) {
        val registerProfile = RegisterResponse(profile = dataRegisterResponse.profile, accessToken = dataRegisterResponse.accessToken)
        Hawk.put("userProfileData", registerProfile)
    }

}