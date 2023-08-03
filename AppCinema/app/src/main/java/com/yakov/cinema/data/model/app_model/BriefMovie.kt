package com.yakov.cinema.data.model.app_model

data class BriefMovie(
    var id: Int,
    var image: String?,
    var name: String?,
    var rating: String?,
    val genre: String?,
)