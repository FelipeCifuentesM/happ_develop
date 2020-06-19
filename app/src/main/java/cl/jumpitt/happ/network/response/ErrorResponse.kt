package cl.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    val error: String? = null,
    @SerializedName("error_message")
    val errorMessage: String? = null
)
