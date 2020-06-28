package com.jumpitt.happ.network.request

import com.google.gson.annotations.SerializedName

data class TracingRequest(
    val tcn: String,
    @SerializedName("tcn_founded")
    val tcnFounded: String,
    val distance: Double?
)