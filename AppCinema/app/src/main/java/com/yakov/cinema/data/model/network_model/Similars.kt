package com.yakov.cinema.data.model.network_model


import com.google.gson.annotations.SerializedName

data class Similars(
    @SerializedName("items") val items: List<Item>,
    @SerializedName("total") val total: Int
)

data class Item(
    @SerializedName("filmId") val filmId: Int,
    @SerializedName("nameEn") val nameEn: String,
    @SerializedName("nameOriginal") val nameOriginal: String,
    @SerializedName("nameRu") val nameRu: String,
    @SerializedName("posterUrl") val posterUrl: String,
    @SerializedName("posterUrlPreview") val posterUrlPreview: String,
    @SerializedName("relationType") val relationType: String
)

