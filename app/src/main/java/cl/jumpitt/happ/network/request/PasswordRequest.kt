package cl.jumpitt.happ.network.request

import com.google.gson.annotations.SerializedName

data class RecoverPasswordRequest (
    val email: String?
)

data class ChangePasswordRequest(
    @SerializedName("old_password")
    val oldPassword: String,
    @SerializedName("new_password")
    val newPassword: String
)