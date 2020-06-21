package cl.jumpitt.happ.ui.recoverPassword

import cl.jumpitt.happ.network.RestClient
import cl.jumpitt.happ.network.request.RecoverPasswordRequest
import cl.jumpitt.happ.network.response.RecoverPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecoverPasswordInteractor(private val mIOutput: RecoverPasswordContract.InteractorOutputs): RecoverPasswordContract.Interactor {

    override fun postForgotPassword(recoverPasswordRequest: RecoverPasswordRequest) {
        RestClient.instance.postForgotPassword(recoverPasswordRequest).
        enqueue(object: Callback<RecoverPasswordResponse> {
            override fun onFailure(call: Call<RecoverPasswordResponse>, t: Throwable) {
                mIOutput.postRecoverPassFailureError()
            }

            override fun onResponse(call: Call<RecoverPasswordResponse>,response: Response<RecoverPasswordResponse>) {
                val responseCode = response.code()
                val responseData = response.body()

                when (responseCode) {
                    200 -> {
                        responseData?.let {
                            mIOutput.postRecoverPassOutput(responseData)
                        }?: run {
                            mIOutput.postRecoverPassOutputError(responseCode, response)
                        }
                    }
                    else -> {
                        mIOutput.postRecoverPassOutputError(responseCode, response)
                    }
                }
            }
        })
    }

}