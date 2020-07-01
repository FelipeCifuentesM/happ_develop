package com.jumpitt.happ.ui.recoverPassword

import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.RecoverPasswordRequest
import com.jumpitt.happ.network.response.RecoverPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecoverPasswordInteractor(private val mIOutput: RecoverPasswordContract.InteractorOutputs): RecoverPasswordContract.Interactor {

    override fun postForgotPassword(contentType: String, email: String) {
        RestClient.instance.postForgotPassword("application/json", contentType, email).
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