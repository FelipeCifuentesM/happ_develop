package com.jumpitt.happ.network.request

import com.google.gson.annotations.SerializedName

data class TriageAnswerRequest(
    val answers: ArrayList<ChoiceID>
)

data class ChoiceID(
    @SerializedName("choice_id")
    val choiceId: String
)