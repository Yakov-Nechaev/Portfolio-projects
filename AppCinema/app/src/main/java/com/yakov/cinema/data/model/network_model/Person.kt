package com.yakov.cinema.data.model.network_model


import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("age") val age: Int,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("birthplace") val birthplace: String,
    @SerializedName("death") val death: Any?,
    @SerializedName("deathplace") val deathplace: Any?,
    @SerializedName("facts") val facts: List<String>,
    @SerializedName("films") val films: List<FilmPerson>,
    @SerializedName("growth") val growth: Int,
    @SerializedName("hasAwards") val hasAwards: Int,
    @SerializedName("nameEn") val nameEn: String,
    @SerializedName("nameRu") val nameRu: String,
    @SerializedName("personId") val personId: Int,
    @SerializedName("posterUrl") val posterUrl: String,
    @SerializedName("profession") val profession: String,
    @SerializedName("sex") val sex: String,
    @SerializedName("spouses") val spouses: List<Any>,
    @SerializedName("webUrl") val webUrl: String
)


data class FilmPerson(
    @SerializedName("description") val description: String?,
    @SerializedName("filmId") val filmId: Int?,
    @SerializedName("general") val general: Boolean?,
    @SerializedName("nameEn") val nameEn: String?,
    @SerializedName("nameRu") val nameRu: String?,
    @SerializedName("professionKey") val professionKey: String?,
    @SerializedName("rating") val rating: String?
)

