package com.jumpitt.happ.ui.registerPermissions

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.realm.RegisterData
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

}