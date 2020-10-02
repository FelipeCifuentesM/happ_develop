package com.jumpitt.happ.network.request

import com.google.gson.annotations.SerializedName

data class PingUserActiveRequest(
    @SerializedName("device_id") val deviceId: String
)