package com.jumpitt.happ.network.response


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("dni")
    val rut: String?,
    @SerializedName("names")
    val names: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("company")
    val company: Company?,
    @SerializedName("home")
    val home: Home?,
    @SerializedName("work")
    val work: Work?
)

data class Company(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)

data class Home(
    val id: String?,
    val name: String?
)

data class Work(
    val id: String?,
    val name: String?
)
