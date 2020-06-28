package com.jumpitt.happ.network.response

import com.google.gson.annotations.SerializedName

data class RegionsResponse(
    val regions: List<DataRegions>
){
    data class DataRegions(
        @SerializedName("name")
        val name: String,
        val code: String,
        val communes:  List<Communes>
    ){
        data class Communes(
            val id: String,
            val name: String,
            val code: String
        )
    }
}