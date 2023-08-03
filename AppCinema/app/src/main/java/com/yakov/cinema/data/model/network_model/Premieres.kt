package com.yakov.cinema.data.model.network_model


import com.google.gson.annotations.SerializedName

data class Premieres(
    @SerializedName("items") val items: List<ItemX>,
    @SerializedName("total") val total: Int
)

data class ItemX(
    @SerializedName("countries") val countries: List<Country>,
    @SerializedName("duration") val duration: Int,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("kinopoiskId") val kinopoiskId: Int,
    @SerializedName("nameEn") val nameEn: String,
    @SerializedName("nameRu") val nameRu: String,
    @SerializedName("posterUrl") val posterUrl: String,
    @SerializedName("posterUrlPreview") val posterUrlPreview: String,
    @SerializedName("premiereRu") val premiereRu: String,
    @SerializedName("year") val year: Int
)