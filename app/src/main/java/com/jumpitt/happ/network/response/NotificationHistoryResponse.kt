package com.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class NotificationHistoryResponse(
    val notifications: List<Notification>?,
    @SerializedName("current_page")
    val currentPage: Int = 0,
    @SerializedName("per_page")
    val perPage: Int = 0,
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("last_page")
    val lastPage: Int = 0
)

data class Notification(
    val date: String?,
    @SerializedName("date_verbose")
    val dateVerbose: String?,
    var data: List<Data>?
)

data class Data(
    val title: String?,
    val body: String?,
    val type: String?,
    val time: String?,
    @SerializedName("created_at")
    val createdAt: String?
)