package com.jumpitt.happ.network.request

import com.google.gson.annotations.SerializedName

data class LoginAccessTokenRequest(
    @SerializedName("grant_type")
    var grantType: String = "",
    @SerializedName("client_id")
    var clientId: String = "",
    @SerializedName("client_secret")
    var clientSecret: String = "",
    var username: String = "",
    var password: String = ""
)