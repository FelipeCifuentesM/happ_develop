package com.jumpitt.happ.network

import com.jumpitt.happ.network.request.RequestTokenRequest
import com.jumpitt.happ.network.response.RefreshTokenResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.utils.ConstantsApi
import io.realm.Realm
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException


class CustomAuthenticator : Authenticator{
    private val realm = Realm.getDefaultInstance()
    private val registerResponse = realm.where(RegisterData::class.java).findFirst()

    @Throws(IOException::class)
    override fun authenticate(route: Route?, responseAuthenticate: Response): Request? {

        registerResponse?.accessToken
        registerResponse?.refreshToken
        if(registerResponse?.refreshToken == null){
            return  null
        }
        val requestTokenRequest = RequestTokenRequest(grantType = "refresh_token", clientId = ConstantsApi.CLIENT_ID, clientSecret = ConstantsApi.CLIENT_SECRET, refreshToken = registerResponse.refreshToken)
        val retrofitResponse: retrofit2.Response<RefreshTokenResponse> =
            RestClient.instance.postRefreshAccessToken(requestTokenRequest).execute()
        val refreshTokenResponse: RefreshTokenResponse? = retrofitResponse.body()

        return if(refreshTokenResponse != null){
            val newAccessToken: String = refreshTokenResponse.accessToken
            responseAuthenticate.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        }else{
            null
        }
    }

}
