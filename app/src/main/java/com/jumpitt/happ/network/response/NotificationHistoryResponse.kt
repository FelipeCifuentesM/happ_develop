package com.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class NotificationHistoryResponse(
    val notifications: List<Notification>?
)

data class Notification(
    val date: String?,
    @SerializedName("date_verbose")
    val dateVerbose: String?,
    val data: List<Data>?
)

data class Data(
    val title: String?,
    val body: String?,
    val type: String?,
    @SerializedName("created_at")
    val createdAt: String?
)