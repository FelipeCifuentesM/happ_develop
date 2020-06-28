package com.jumpitt.happ.network.request


import com.google.gson.annotations.SerializedName

data class RequestTokenRequest(
    @SerializedName("grant_type")
    var grantType: String = "",
    @SerializedName("client_id")
    var clientId: String = "",
    @SerializedName("client_secret")
    var clientSecret: String = "",
    @SerializedName("refresh_token")
    var refreshToken: String = "",
    var scope: String = ""
)