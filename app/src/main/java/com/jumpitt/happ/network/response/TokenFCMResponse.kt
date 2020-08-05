package com.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class TokenFCMResponse(
    val id: String?,
    @SerializedName("user_id")
    val userId: String?,
    val status: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)