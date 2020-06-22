package cl.jumpitt.happ.network

import cl.jumpitt.happ.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClient{
    private const val CONNECT_TIMEOUT = 30
    private const val WRITE_TIMEOUT = 30
    private const val READ_TIMEOUT = 30
    private const val BASE_URL = BuildConfig.BASE_URL

    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)


    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(loggingInterceptor)
        .authenticator(AccessTokenAuthenticator())
        .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .build()


    val instance: ApiService by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(ApiService::class.java)
    }

    //mocky borrar
    val instance2: ApiService by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(ApiService::class.java)
    }

}