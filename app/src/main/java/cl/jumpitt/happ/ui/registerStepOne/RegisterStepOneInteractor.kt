package cl.jumpitt.happ.ui.registerStepOne

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import cl.jumpitt.happ.network.RestClient
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.request.ValidateDNIRequest
import cl.jumpitt.happ.network.response.ErrorResponse
import cl.jumpitt.happ.network.response.ValidateDNIResponse
import cl.jumpitt.happ.utils.ConstantsApi
import cl.jumpitt.happ.utils.parseErrJsonResponse
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterStepOneInteractor: RegisterStepOneContract.Interactor{

    override fun postValidateDNI(validateDNIRequest: ValidateDNIRequest, interactorOutputs: RegisterStepOneContract.InteractorOutputs) {
        RestClient.instance.postValidateDNI(validateDNIRequest).
        enqueue(object: Callback<ValidateDNIResponse> {
            override fun onFailure(call: Call<ValidateDNIResponse>, t: Throwable) {
                interactorOutputs.postValidateDNIFailureError()
            }

            override fun onResponse(call: Call<ValidateDNIResponse>,response: Response<ValidateDNIResponse>) {
                val responseCode = response.code()
                val responseData = response.body()

                when (responseCode) {
                    200 -> {
                        responseData?.let {
                            interactorOutputs.postValidateDNIOutput(validateDNIRequest)
                        }?: run {
                            interactorOutputs.postValidateDNIOutputError(responseCode, response)
                        }
                    }
                    else -> {
                        interactorOutputs.postValidateDNIOutputError(responseCode, response)
                    }
                }
            }
        })
    }

    override fun saveRegisterData(registerDataObject: RegisterRequest) {
        Hawk.put("registerData", registerDataObject)
    }

}