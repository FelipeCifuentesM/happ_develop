package com.jumpitt.happ.ui.triage


import com.jumpitt.happ.model.Triage
import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.ChoiceID
import com.jumpitt.happ.network.request.TriageAnswerRequest
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.realm.TriageReturnValue
import com.jumpitt.happ.utils.ConstantsApi
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TriageActivityInteractor(private val mIOutput: TriageActivityContract.InteractorOutputs) :
    TriageActivityContract.Interactor {
    private lateinit var mTriage: Triage
    private var actualResponses = arrayListOf<String>()
    private var triageAnswerResponse = ArrayList<ChoiceID>()
    private var mIndex = -1

    override fun getToken(){
        val realm = Realm.getDefaultInstance()
        var accessToken = realm.where(RegisterData::class.java).findFirst()?.accessToken

        if(accessToken.isNullOrBlank())
            accessToken = ""
        mIOutput.getTokenOutput(accessToken)
    }

    override fun getTriage(accessToken: String) {
        RestClient.instance.getTriage("${ConstantsApi.BEARER} $accessToken").
        enqueue(object: Callback<Triage> {
            override fun onFailure(call: Call<Triage>, t: Throwable) {
            }

            override fun onResponse(call: Call<Triage>, response: Response<Triage>) {
                val dataResponse = response.body()
                if (dataResponse != null) {
                    mTriage = dataResponse
                    mIOutput.onTriageLoaded()
                }
            }
        })

    }

    override fun nextQuestionRequested(responses: List<String>?, tracing: Boolean) {
        responses?.let { actualResponses.addAll(it) }
        mIndex++
        mIOutput.nextQuestion(if (mIndex < mTriage.questions.size) mTriage.questions[mIndex] else null, tracing)
    }

    override fun sendTriageAnswers(tracing: Boolean, accessToken: String) {
        //ENVIAR RESPUESTAS
        actualResponses.let { _actualResponses ->
            _actualResponses.forEach { _triageAnswer ->
                val objectAnswer = ChoiceID(_triageAnswer)
                triageAnswerResponse.add(objectAnswer)
            }
        }
        val triageAnswerResponse = TriageAnswerRequest(triageAnswerResponse)
        val accessToken = "${ConstantsApi.BEARER} $accessToken"
        RestClient.instance.postTriageAnswer( accessToken, triageAnswerResponse).
        enqueue(object: Callback<TriageAnswerResponse>{
            override fun onFailure(call: Call<TriageAnswerResponse>, t: Throwable) {
                mIOutput.getTriageAnswerFailureError()
            }

            override fun onResponse(call: Call<TriageAnswerResponse>, response: Response<TriageAnswerResponse>) {
                val dataResponse = response.body()
                when (response.code()) {
                    200 -> {
                        dataResponse?.let { responseTriageAnswer ->
                            mIOutput.getTriageAnswerOutput(tracing, responseTriageAnswer)
                        }?: run {
                            mIOutput.getTriageAnswerOutputError(response.code(), response)
                        }
                    }
                    else -> {
                        mIOutput.getTriageAnswerOutputError(response.code(), response)
                    }
                }
            }

        })
    }

    override fun getAccessTokenProfile(tracing: Boolean){
        val realm = Realm.getDefaultInstance()
        var accessToken = realm.where(RegisterData::class.java).findFirst()?.accessToken

        if(accessToken.isNullOrBlank())
            accessToken = ""
        mIOutput.getAccessTokenProfileOutput(tracing, accessToken)
    }

    override fun saveResult(healthCareStatusRealm: TriageReturnValue) {
        //GUARDAR DATOS LOCAL
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(TriageReturnValue::class.java)
        realm.insertOrUpdate(healthCareStatusRealm)
        realm.commitTransaction()
        realm.close()

    }


}