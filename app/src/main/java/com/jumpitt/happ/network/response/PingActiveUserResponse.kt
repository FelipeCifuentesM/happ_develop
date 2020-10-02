package com.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class PingActiveUserResponse(
    @SerializedName("refresh") val refresh: Int?
)