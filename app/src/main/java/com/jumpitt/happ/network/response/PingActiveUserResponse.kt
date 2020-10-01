package com.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class PingActiveUserResponse(
    @SerializedName("valid_token") val validToken: Int?,
    @SerializedName("request_time") val requestTime: Int?
)