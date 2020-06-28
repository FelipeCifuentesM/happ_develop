package com.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class TracingResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: String,
    @SerializedName("tcn") val tcn: String,
    @SerializedName("tcn_founded") var tcnFound: String,
    @SerializedName("distance") val type: Int
)