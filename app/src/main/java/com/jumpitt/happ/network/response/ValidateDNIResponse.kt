package com.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName


data class ValidateDNIResponse(
    val id: String? = null,
    @SerializedName("dni")
    val rut: String? = null,
    val status: String? = null
)