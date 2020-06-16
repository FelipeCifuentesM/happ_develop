package cl.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName


data class ValidateDNIResponse(
    val code: Int,
    val resource: String,
    val message: String,
    val id: String,
    @SerializedName("dni")
    val rut: String,
    val status: String?
)