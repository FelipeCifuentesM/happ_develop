package cl.jumpitt.happ.network.request

import com.google.gson.annotations.SerializedName

data class TracingRequest(
    @SerializedName("user_id")
    val userId: String,
    val tcn: String,
    @SerializedName("tcn_founded")
    val tcnFounded: String,
    val distance: String
)