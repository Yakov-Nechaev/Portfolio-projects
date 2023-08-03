package com.yakov.cinema.data.model.app_model

import com.yakov.cinema.data.model.network_model.FilmPerson

data class PersonnelDetails(
    val id: Int,
    val photo: String,
    val name: String,
    val profession: String,
    var bestMovie: MutableList<BriefMovie> = mutableListOf(),
    val allIdMovieId: MutableList<AllIdMovie> = mutableListOf(),
    val allMovie: MutableList<BriefMovie> = mutableListOf(),
    val listMovieFull: List<FilmPerson> = emptyList()
)

data class AllIdMovie(
    val id: Int
)










