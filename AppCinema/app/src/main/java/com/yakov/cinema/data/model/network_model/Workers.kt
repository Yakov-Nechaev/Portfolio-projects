package com.yakov.cinema.data.model.network_model


import com.google.gson.annotations.SerializedName

class Workers : ArrayList<WorkersItem>()

data class WorkersItem(
    @SerializedName("description") val description: String?,
    @SerializedName("nameEn") val nameEn: String,
    @SerializedName("nameRu") val nameRu: String,
    @SerializedName("posterUrl") val posterUrl: String,
    @SerializedName("professionKey") val professionKey: String,
    @SerializedName("professionText") val professionText: String,
    @SerializedName("staffId") val staffId: Int
)

