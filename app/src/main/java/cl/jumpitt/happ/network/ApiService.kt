package cl.jumpitt.happ.network


import cl.jumpitt.happ.model.Triage
import cl.jumpitt.happ.network.request.*
import cl.jumpitt.happ.network.response.*
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

    @GET("profile")
    fun getProfile(@Header("Authorization") accessToken: String): Call<ProfileResponse>

    @GET("triage")
    fun getTriage(@Header("Authorization") accessToken: String): Call<Triage>

    @POST("triage/answer")
    fun postTriageAnswer(@Header("Authorization") accessToken: String, @Body triageAnswerRequest: TriageAnswerRequest): Call<TriageAnswerResponse>

    @GET("healthcare")
    fun getHealthCare(@Header("Authorization") accessToken: String): Call<TriageAnswerResponse>

    @POST
    fun postTCN(@Url url: String, @Body tcnRequest: TracingRequest): Call<TracingResponse>

    @POST("password/forgot")
    fun postForgotPassword(@Body recoverPasswordRequest: RecoverPasswordRequest): Call<RecoverPasswordResponse>

    @PUT("password")
    fun putChangePassword(@Header("Authorization") accessToken: String, @Body changePasswordRequest: ChangePasswordRequest): Call<ChangePasswordResponse>
}