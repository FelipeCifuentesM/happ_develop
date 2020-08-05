package com.jumpitt.happ.ui.profile

import android.util.Log
import com.jumpitt.happ.App
import com.jumpitt.happ.ble.BleManagerImpl
import com.jumpitt.happ.ble.TcnGeneratorImpl
import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.network.response.TokenFCMResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.realm.TriageReturnValue
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragmentInteractor(private val mIOutput: ProfileFragmentContract.InteractorOutputs): ProfileFragmentContract.Interactor {

    override fun getUserProfileData() {
        val realm = Realm.getDefaultInstance()
        val userData = realm.where(RegisterData::class.java).findFirst()
        mIOutput.getUserProfileDataOutput(userData)
    }

    override fun getAccessToken() {
        val realm = Realm.getDefaultInstance()
        var accessToken = realm.where(RegisterData::class.java).findFirst()?.accessToken

        if(accessToken.isNullOrBlank())
            accessToken = ""
        mIOutput.getAccesTokenOutput(accessToken)
    }

    override fun deleteRegisterTokenFCM(accessToken: String, tokenFCMRequest: TokenFCMRequest) {
        Log.e("Borrar", "token diospositivo6: "+tokenFCMRequest.token)
        RestClient.instance.deleteRegisterTokenFCM2(accessToken, tokenFCMRequest).
        enqueue(object: Callback<TokenFCMResponse> {
            override fun onFailure(call: Call<TokenFCMResponse>, t: Throwable) {
                mIOutput.postRegisterTokenFCMFailureError()
            }

            override fun onResponse(call: Call<TokenFCMResponse>, response: Response<TokenFCMResponse>) {
                val responseCode = response.code()
                val responseData = response.body()

                mIOutput.postRegisterTokenFCMOutput()
            }
        })
    }


    override fun deleteProfileData() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(RegisterData::class.java)
        realm.delete(TriageReturnValue::class.java)
        realm.commitTransaction()
        realm.close()

        val tcnGenerator = TcnGeneratorImpl(context = App.ctx!!)
        val bleManagerImpl = BleManagerImpl(
            app = App.ctx!!,
            tcnGenerator = tcnGenerator
        )
        bleManagerImpl.stopService()
    }


}