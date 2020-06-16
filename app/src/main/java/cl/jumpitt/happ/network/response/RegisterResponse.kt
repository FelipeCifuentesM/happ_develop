package cl.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val profile: ProfileResponse? = null,
    @SerializedName("access_token")
    val accessToken: String? = null
)