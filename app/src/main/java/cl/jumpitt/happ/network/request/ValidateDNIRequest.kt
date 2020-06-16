package cl.jumpitt.happ.network.request

import com.google.gson.annotations.SerializedName

data class ValidateDNIRequest(
    val dni: String,
    @SerializedName("document_number")
    val documentNumber: String
)