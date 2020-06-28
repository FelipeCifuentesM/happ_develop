package com.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class LoginAccessTokenResponse(
    val code: Int,
    val resource: String,
    val message: String,
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("expires_in")
    val expiresIn: Int?,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)