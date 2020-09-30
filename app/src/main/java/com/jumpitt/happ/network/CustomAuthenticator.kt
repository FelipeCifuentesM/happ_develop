package com.jumpitt.happ.network

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.jumpitt.happ.App
import com.jumpitt.happ.ble.BleManagerImpl
import com.jumpitt.happ.ble.TcnGeneratorImpl
import com.jumpitt.happ.context
import com.jumpitt.happ.network.request.RequestTokenRequest
import com.jumpitt.happ.network.response.RefreshTokenResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.realm.TraceProximityNotification
import com.jumpitt.happ.realm.TriageReturnValue
import com.jumpitt.happ.ui.login.Login
import com.jumpitt.happ.utils.ConstantsApi
import com.jumpitt.happ.utils.goToActivity
import com.jumpitt.happ.utils.transitionActivity
import io.realm.Realm
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class CustomAuthenticator : Authenticator{
    private var count = 0

    @Throws(IOException::class)
    override fun authenticate(route: Route?, responseAuthenticate: Response): Request? {
        synchronized(this) {
//            Log.e("Borrar", "Contador: " + count)
            if (count > 1) {
                deleteProfileData()
                return null
            }
            count++
            Realm.init(context)
            val realm = Realm.getDefaultInstance()
            val registerResponse = realm.where(RegisterData::class.java).findFirst()

            val refreshToken = registerResponse?.refreshToken
            realm.close()
            if (refreshToken == null) {
                return null
            }

            val requestTokenRequest = RequestTokenRequest(
                grantType = "refresh_token",
                clientId = ConstantsApi.CLIENT_ID,
                clientSecret = ConstantsApi.CLIENT_SECRET,
                refreshToken = refreshToken
            )
            val retrofitResponse: retrofit2.Response<RefreshTokenResponse> =
                RestClient.instance.postRefreshAccessToken(requestTokenRequest).execute()
            val refreshTokenResponse: RefreshTokenResponse? = retrofitResponse.body()

            if (refreshTokenResponse != null) {
                count = 0
                val newAccessToken: String = refreshTokenResponse.accessToken
                val responseAuth = responseAuthenticate.request.newBuilder()
                    .header("Authorization", "${ConstantsApi.BEARER} $newAccessToken")
                    .build()

                updateUserDataRealm(refreshTokenResponse)
                return responseAuth
            } else {
                count++
                return null
            }
        }
    }

    private fun updateUserDataRealm(refreshTokenResponse: RefreshTokenResponse){
        val realm:Realm = Realm.getDefaultInstance()
        val registerResponse: RegisterData? = realm.where(RegisterData::class.java).findFirst()
        realm.beginTransaction()
        var userData = RegisterData()
        registerResponse?.let {
            userData = it
            registerResponse.accessToken?.let { userData.accessToken  = refreshTokenResponse.accessToken }
            registerResponse.refreshToken?.let { userData.refreshToken = refreshTokenResponse.refreshToken }
            realm.insertOrUpdate(userData)
            realm.commitTransaction()
        }
        realm.close()
    }

    private fun navigateLogin() {
        val intent = Intent(context, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(context, intent, Bundle())
    }

    private fun deleteProfileData() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(RegisterData::class.java)
        realm.delete(TriageReturnValue::class.java)
        realm.delete(TraceProximityNotification::class.java)
        realm.commitTransaction()
        realm.close()

        val tcnGenerator = TcnGeneratorImpl(context = App.ctx!!)
        val bleManagerImpl = BleManagerImpl(
            app = App.ctx!!,
            tcnGenerator = tcnGenerator
        )
        bleManagerImpl.stopService()

        navigateLogin()
    }

}
