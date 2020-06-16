package cl.jumpitt.happ.ui.triage


import cl.jumpitt.happ.model.Triage
import cl.jumpitt.happ.network.RestClient
import cl.jumpitt.happ.network.request.ChoiceID
import cl.jumpitt.happ.network.request.TriageAnswerRequest
import cl.jumpitt.happ.network.response.RegisterResponse
import cl.jumpitt.happ.network.response.TriageAnswerResponse
import cl.jumpitt.happ.utils.ConstantsApi
import com.orhanobut.hawk.Hawk
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
        var accessToken = Hawk.get<RegisterResponse>("userProfileData").accessToken
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
                mIOutput.getTriageAnswerOutputError(-1)
            }

            override fun onResponse(call: Call<TriageAnswerResponse>, response: Response<TriageAnswerResponse>) {
                val dataResponse = response.body()
                when (response.code()) {
                    200 -> {
                        dataResponse?.let { responseTriageAnswer ->
                            mIOutput.getTriageAnswerOutput(tracing, responseTriageAnswer)
                        }?: run {
                            mIOutput.getTriageAnswerOutputError(response.code())
                        }
                    }
                    422 -> {
                        mIOutput.getTriageAnswerOutputError(response.code())
                    }
                    else -> {
                        mIOutput.getTriageAnswerOutputError(response.code())
                    }
                }
            }

        })

    }

    override fun getAccessTokenProfile(tracing: Boolean){
        var accessToken = Hawk.get<RegisterResponse>("userProfileData").accessToken
        if(accessToken.isNullOrBlank())
            accessToken = ""
        mIOutput.getAccessTokenProfileOutput(tracing, accessToken)
    }

    override fun saveResult(responseTriageAnswer: TriageAnswerResponse) {
        //GUARDAR DATOS LOCAL
        Hawk.put("triageReturnValue", responseTriageAnswer)
    }


}