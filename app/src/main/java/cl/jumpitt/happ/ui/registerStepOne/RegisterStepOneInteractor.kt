package cl.jumpitt.happ.ui.registerStepOne

import cl.jumpitt.happ.network.RestClient
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.request.ValidateDNIRequest
import cl.jumpitt.happ.network.response.ValidateDNIResponse
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterStepOneInteractor: RegisterStepOneContract.Interactor{

    override fun postValidateDNI(validateDNIRequest: ValidateDNIRequest, interactorOutputs: RegisterStepOneContract.InteractorOutputs) {
        RestClient.instance.postValidateDNI(validateDNIRequest).
        enqueue(object: Callback<ValidateDNIResponse> {
            override fun onFailure(call: Call<ValidateDNIResponse>, t: Throwable) {
                interactorOutputs.postValidateDNIOutputError()
            }

            override fun onResponse(call: Call<ValidateDNIResponse>,response: Response<ValidateDNIResponse>) {
                val responseCode = response.code()
                val responseData = response.body()

                responseData?.let {
                    if(responseCode == 200){
                        interactorOutputs.postValidateDNIOutput(validateDNIRequest)
                    }else{
                        interactorOutputs.postValidateDNIOutputError()
                    }
                }?: run {
                    interactorOutputs.postValidateDNIOutputError()
                }
            }
        })
    }

    override fun saveRegisterData(registerDataObject: RegisterRequest) {
        Hawk.put("registerData", registerDataObject)
    }

}