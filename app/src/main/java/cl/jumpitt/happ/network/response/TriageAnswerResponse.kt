package cl.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class TriageAnswerResponse(
    @SerializedName("triage_status")
    val triageStatus: String?,
    @SerializedName("score")
    val score: Int?,
    @SerializedName("risk")
    val risk: Risk?,
    @SerializedName("latest_review")
    val latestReview: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("passport")
    val passport: Passport?
)

data class Risk(
    @SerializedName("level")
    val level: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("message")
    val message: String?
)

data class Passport(
    @SerializedName("code")
    val code: String?,
    @SerializedName("validation_url")
    val validationUrl: String?,
    @SerializedName("time_remaining")
    val timeRemaining: String?,
    @SerializedName("time_remaining_verbose")
    val timeRemainingVerbose: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("issued_at")
    val issuedAt: String?
)