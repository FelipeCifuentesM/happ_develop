package cl.jumpitt.happ.network


import cl.jumpitt.happ.model.Triage
import cl.jumpitt.happ.network.request.LoginAccessTokenRequest
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.request.TriageAnswerRequest
import cl.jumpitt.happ.network.request.ValidateDNIRequest
import cl.jumpitt.happ.network.response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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

}