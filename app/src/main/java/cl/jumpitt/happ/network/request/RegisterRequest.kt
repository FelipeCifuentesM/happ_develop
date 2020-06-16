package cl.jumpitt.happ.network.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    var dni: String? = null,
    var names: String? = null,
    @SerializedName("last_name")
    var lastName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var phone: String? = null,
    @SerializedName("home_commune_id")
    var homeCommuneId: String? = null,
    @SerializedName("work_commune_id")
    var workCommuneId: String? = null
)