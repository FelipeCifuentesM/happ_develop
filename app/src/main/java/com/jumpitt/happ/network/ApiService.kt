package com.jumpitt.happ.network


import com.jumpitt.happ.model.Triage
import com.jumpitt.happ.network.request.*
import com.jumpitt.happ.network.response.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("places")
    fun getRegions(): Call<RegionsResponse>

    @POST("auth/register/dni")
    fun postValidateDNI(@Body validateDNIRequest: ValidateDNIRequest): Call<ValidateDNIResponse>

    @POST("auth/register/profile")
    fun postRegister(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("auth/token")
    fun postLoginAccessToken(@Body loginTokenRequest: LoginAccessTokenRequest): Call<LoginAccessTokenResponse>

    @POST("auth/token")
    fun postRefreshAccessToken(@Body requestTokenRequest: RequestTokenRequest): Call<RefreshTokenResponse>

    @GET("profile")
    fun getProfile(@Header("Authorization") accessToken: String): Call<ProfileResponse>

    @GET("triage")
    fun getTriage(@Header("Authorization") accessToken: String): Call<Triage>

    @POST("triage/answer")
    fun postTriageAnswer(@Header("Authorization") accessToken: String, @Body triageAnswerRequest: TriageAnswerRequest): Call<TriageAnswerResponse>

    @GET("healthcare")
    fun getHealthCare(@Header("Authorization") accessToken: String): Call<TriageAnswerResponse>

    @POST
    fun postTCN(@Url url: String, @Header("Authorization") accessToken: String, @Body tcnRequest: TracingRequest): Call<TracingResponse>

    @FormUrlEncoded
    @POST("password/forgot")
    fun postForgotPassword(@Header ("Accept") accept: String, @Header ("Content-Type") contentType: String,   @Field("email") email: String): Call<RecoverPasswordResponse>

    @PUT("password")
    fun putChangePassword(@Header("Authorization") accessToken: String, @Body changePasswordRequest: ChangePasswordRequest): Call<ChangePasswordResponse>

    @POST("devices/token")
    fun postRegisterTokenFCM(@Header("Authorization") accessToken: String, @Body requestTokenFCM: TokenFCMRequest): Call<TokenFCMResponse>

    @HTTP(method = "DELETE", path = "devices/token", hasBody = true)
    fun deleteRegisterTokenFCM2(@Header("Authorization") accessToken: String, @Body requestTokenFCM: TokenFCMRequest): Call<TokenFCMResponse>

//    @GET("v3/c7c5f811-ed54-4abe-bb26-02115d5d079e")
//    fun getNotificationHistory(): Call<NotificationHistoryResponse>

    @GET("notifications")
    fun getNotificationHistory(@Header("Authorization") accessToken: String, @Query("page") currentPage: Int): Call<NotificationHistoryResponse>

    @POST("pings")
    fun getPingUserActive(@Header("Authorization") accessToken: String): Call<PingActiveUserResponse>

}