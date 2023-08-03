package com.yakov.cinema.data.model.app_model

data class CollectionMovie(
    var name: String,
    var listMovie: List<AllIdMovie> = emptyList()
)
