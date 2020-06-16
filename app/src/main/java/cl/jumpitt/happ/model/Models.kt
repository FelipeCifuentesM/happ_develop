package cl.jumpitt.happ.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class DemoTracing(
        var tcn: String,
        var date: String,
        var distance: String
)

data class TriageResponse(
    @SerializedName("status") var status: String,
    @SerializedName("results") var results: TriageResult? = null,
    @SerializedName("triage") var triage: Triage
)

data class TriageResult(
    @SerializedName("last_triage_id") val lastId: String?,
    @SerializedName("last_triage_data") val lastDate: String?,
    @SerializedName("infected_level") var level: Int
)


data class Triage(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String,
    @SerializedName("questions") val questions: ArrayList<Question>,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class Question(
    @SerializedName("id") val id: String,
    @SerializedName("number") val order: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") var description: String?,
    @SerializedName("type") val type: String,
    @SerializedName("choices") val choices: ArrayList<Choice>
)

data class Choice(
    @SerializedName("id") val id: String,
    @SerializedName("value") val value: String,
    @SerializedName("description") var description: String?,
    @SerializedName("priority") val priority: Int,
    @SerializedName("selected") val selected: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt() == 1
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(value)
        parcel.writeString(description)
        parcel.writeInt(priority)
        parcel.writeInt(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Choice> {
        override fun createFromParcel(parcel: Parcel): Choice {
            return Choice(parcel)
        }

        override fun newArray(size: Int): Array<Choice?> {
            return arrayOfNulls(size)
        }
    }
}