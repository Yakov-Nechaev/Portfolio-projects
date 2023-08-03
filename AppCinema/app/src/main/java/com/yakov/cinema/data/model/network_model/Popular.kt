package com.yakov.cinema.data.model.network_model


import com.google.gson.annotations.SerializedName

data class Popular(
    @SerializedName("films") val films: List<FilmItem>,
    @SerializedName("pagesCount") val pagesCount: Int
)

data class FilmItem(
    @SerializedName("countries") val countries: List<Country>,
    @SerializedName("filmId") val filmId: Int,
    @SerializedName("filmLength") val filmLength: String?,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("isAfisha") val isAfisha: Int,
    @SerializedName("isRatingUp") val isRatingUp: Any?,
    @SerializedName("nameEn") val nameEn: String?,
    @SerializedName("nameRu") val nameRu: String,
    @SerializedName("posterUrl") val posterUrl: String,
    @SerializedName("posterUrlPreview") val posterUrlPreview: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("ratingChange") val ratingChange: Any?,
    @SerializedName("ratingVoteCount") val ratingVoteCount: Int,
    @SerializedName("year") val year: String
)