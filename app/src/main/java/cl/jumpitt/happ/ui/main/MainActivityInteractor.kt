package cl.jumpitt.happ.ui.main

import cl.jumpitt.happ.network.RestClient
import cl.jumpitt.happ.network.response.RegisterResponse
import cl.jumpitt.happ.network.response.TriageAnswerResponse
import cl.jumpitt.happ.utils.ConstantsApi
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityInteractor(private val mIOutput: MainActivityContract.InteractorOutputs): MainActivityContract.Interactor {

    override fun getAccessToken() {
        var accessToken = Hawk.get<RegisterResponse>("userProfileData").accessToken
        if(accessToken.isNullOrBlank())
            accessToken = ""
        mIOutput.getAccesTokenOutput(accessToken)
    }

    override fun getHealthCare(accessToken: String) {
        RestClient.instance.getHealthCare("${ConstantsApi.BEARER} $accessToken").
        enqueue(object: Callback<TriageAnswerResponse> {
            override fun onFailure(call: Call<TriageAnswerResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<TriageAnswerResponse>, response: Response<TriageAnswerResponse>) {
                val dataResponseCode = response.code()
                val dataResponse = response.body()

                if (dataResponseCode == 200){
                    dataResponse?.let { healthCareStatus ->
                        mIOutput.getHealthCareOutput(healthCareStatus)
                    }?: run{
                        mIOutput.getHealthCareOutputError()
                    }
                }else{
                    mIOutput.getHealthCareOutputError()
                }
            }
        })
    }

    override fun saveHealthCareStatus(healthCareStatus: TriageAnswerResponse) {
        val healthCareStatusLocal = Hawk.get<TriageAnswerResponse>("triageReturnValue")
        healthCareStatusLocal?.let {
            Hawk.delete("triageReturnValue")
        }
        Hawk.put("triageReturnValue", healthCareStatus)
    }

}