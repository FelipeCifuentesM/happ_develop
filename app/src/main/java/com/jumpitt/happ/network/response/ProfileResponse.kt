package com.jumpitt.happ.network.response


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    val id: String?,
    @SerializedName("dni")
    val rut: String?,
    val names: String?,
    @SerializedName("last_name")
    val lastName: String?,
    val email: String?,
    val phone: String?,
    val status: String?,
    val home: Home?,
    val work: Work?
)

data class Home(
    val id: String?,
    val name: String?
)

data class Work(
    val id: String?,
    val name: String?
)
