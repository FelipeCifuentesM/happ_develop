package cl.jumpitt.happ.network

import cl.jumpitt.happ.network.response.LoginAccessTokenResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(private val authorizationRepository: LoginAccessTokenResponse) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().signedRequest()
        return chain.proceed(newRequest)
    }

    private fun Request.signedRequest(): Request {
        val accessToken = authorizationRepository.accessToken
        return newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}