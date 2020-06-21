package cl.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class RecoverPasswordResponse (
    @SerializedName("message")
    val message: String? = null
)

data class ChangePasswordResponse(
    val dni: String
)