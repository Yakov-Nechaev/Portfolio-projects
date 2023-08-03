package com.yakov.cinema.constants_and_extensions

import com.yakov.cinema.data.model.app_model.Actor
import com.yakov.cinema.data.model.app_model.AllIdMovie
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.data.model.app_model.Crew
import com.yakov.cinema.data.model.app_model.DetailsMovie
import com.yakov.cinema.data.model.app_model.ImagesMovie
import com.yakov.cinema.data.model.app_model.Personnel
import com.yakov.cinema.data.model.app_model.PersonnelDetails
import com.yakov.cinema.data.model.network_model.Images
import com.yakov.cinema.data.model.network_model.Movie
import com.yakov.cinema.data.model.network_model.Person
import com.yakov.cinema.data.model.network_model.Popular
import com.yakov.cinema.data.model.network_model.Premieres
import com.yakov.cinema.data.model.network_model.Search
import com.yakov.cinema.data.model.network_model.Similars
import com.yakov.cinema.data.model.network_model.Top
import com.yakov.cinema.data.model.network_model.Workers
import com.yakov.cinema.data.network.NetworkService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

fun Images.mapToListImagesMovie(): List<ImagesMovie> {
    return this.items.map {
        ImagesMovie(urlImage = it.previewUrl)
    }
}

fun Movie.mapToDetailsMovie(): DetailsMovie {
    return DetailsMovie(
        id = this.kinopoiskId ?: 0,
        rating = this.ratingKinopoisk ?: 0.0,
        name = this.nameRu ?: "",
        year = this.year ?: 0,
        genre = genres.firstOrNull()?.genre ?: "",
        country = countries.firstOrNull()?.country ?: "",
        lengthMin = this.filmLength ?: 0,
        shortDescription = this.shortDescription ?: "",
        completeDescription = this.description ?: "",
        image = this.posterUrlPreview ?: ""
    )
}

fun Movie.mapToBriefMovie(): BriefMovie {
    return BriefMovie(
        id = this.kinopoiskId ?: 0,
        image = this.posterUrlPreview ?: "",
        name = this.nameRu ?: "",
        rating = this.ratingKinopoisk.toString(),
        genre = this.genres.firstOrNull()?.genre ?: ""
    )
}


fun Person.mapToPersonnelDetails(): PersonnelDetails {

    val listBestMovie = mutableListOf<BriefMovie>()
    val listAllIdMovie = mutableListOf<AllIdMovie>()

    if (films.isNotEmpty() && films.size >= 5) {
        val sortedListMovie = this.films.sortedByDescending { it.rating }
        for (film in 0..4) {
            val movie = BriefMovie(
                id = sortedListMovie[film].filmId ?:0,
                image = "",
                name = sortedListMovie[film].nameRu.toString(),
                rating = sortedListMovie[film].rating ?: "",
                genre = ""
            )
            listBestMovie.add(movie)
        }
    }

    this.films.forEach {
        if (it.rating != null && it.nameRu != null && it.nameEn != null) {
            val movieAll = AllIdMovie(id = it.filmId ?:0)
            listAllIdMovie.add(movieAll)
        }
    }

    return PersonnelDetails(
        id = personId,
        photo = posterUrl,
        name = nameRu,
        profession = profession,
        bestMovie = listBestMovie.toMutableSet().toMutableList(),
        allIdMovieId = listAllIdMovie,
        listMovieFull = films
    )
}

fun Popular.mapToBriefMovieList(): MutableList<BriefMovie> {
    val briefMoviesList = this.films.map {
        BriefMovie(
            id = it.filmId,
            image = it.posterUrlPreview,
            name = it.nameRu,
            rating = it.rating,
            genre = it.genres.firstOrNull()?.genre ?: ""
        )
    }.toMutableList()
    return briefMoviesList
}

fun Premieres.mapToBriefMovieList(): MutableList<BriefMovie> {
    val briefMoviesList = this.items.map {
        BriefMovie(
            id = it.kinopoiskId,
            image = it.posterUrlPreview,
            name = it.nameRu,
            rating = "",
            genre = it.genres.firstOrNull()?.genre ?: ""
        )
    }.toMutableList()
    return briefMoviesList
}

fun Search.mapToBriefMovieList(): MutableList<BriefMovie> {
    val briefMoviesList = this.items.map {
        BriefMovie(
            id = it.kinopoiskId,
            image = it.posterUrlPreview,
            name = it.nameRu ?: "",
            rating = "",
            genre = it.genres.firstOrNull()?.genre ?: ""
        )
    }.toMutableList()
    return briefMoviesList
}

fun Similars.mapToBriefMovieList(): MutableList<BriefMovie> {
    val briefMoviesList = this.items.map {
        BriefMovie(
            id = it.filmId,
            image = it.posterUrlPreview,
            name = it.nameRu,
            rating = "",
            genre = ""
        )
    }.toMutableList()
    return briefMoviesList
}

fun Top.mapToBriefMovieList(): MutableList<BriefMovie> {
    val briefMoviesList = this.films.map {
        BriefMovie(
            id = it.filmId,
            image = it.posterUrlPreview,
            name = it.nameRu,
            rating = it.rating,
            genre = it.genres.firstOrNull()?.genre ?: ""
        )
    }.toMutableList()
    return briefMoviesList
}


fun Workers.mapToCrewMovie(): Crew {
    val actorList: MutableList<Actor> = mutableListOf()
    val personnelList: MutableList<Personnel> = mutableListOf()

    this.forEach { worker ->
        if (worker.professionKey == "ACTOR") {
            actorList.add(
                Actor(
                    id = worker.staffId,
                    name = worker.nameRu,
                    role = worker.description.toString(),
                    image = worker.posterUrl
                )
            )
        } else {
            personnelList.add(
                Personnel(
                    id = worker.staffId,
                    name = worker.nameRu,
                    role = worker.professionText,
                    image = worker.posterUrl
                )
            )
        }
    }

    return Crew(actorList, personnelList)
}

suspend fun PersonnelDetails.updateBestMovie() {
    coroutineScope {
        launch {
            try {
                bestMovie.forEach {
                    it.image = NetworkService.cinemaService.getMovieById(it.id.toString())
                        .mapToDetailsMovie().image
                }
            } catch (e: Exception) {
                throw (e)
            }
        }
    }
}
