package com.yakov.cinema.data.network

import com.yakov.cinema.constants_and_extensions.mapToBriefMovieList
import com.yakov.cinema.data.model.network_model.Images
import com.yakov.cinema.data.model.network_model.Movie
import com.yakov.cinema.data.model.network_model.Person
import com.yakov.cinema.data.model.network_model.Popular
import com.yakov.cinema.data.model.network_model.Premieres
import com.yakov.cinema.data.model.network_model.Search
import com.yakov.cinema.data.model.network_model.Similars
import com.yakov.cinema.data.model.network_model.Top
import com.yakov.cinema.data.model.network_model.Workers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.system.exitProcess

object NetworkService {

    private const val BASE_URL = "https://kinopoiskapiunofficial.tech"
    private const val api_key = "765266b1-15e5-4fc0-a9bd-d29696b30029"

    private val retrofitCinema = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val cinemaService: CinemaApiService = retrofitCinema.create(CinemaApiService::class.java)

    interface CinemaApiService {

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v2.2/films/premieres")
        suspend fun getPremieres(
            @Query("year") year: Int,
            @Query("month") mont: String,
            @Query("page") page: Int = 1
        ): Premieres

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v2.2/films")
        suspend fun getActionUSA(
            @Query("countries") countries: Int = 1,
            @Query("genres") genres: Int = 11,
            @Query("page") page: Int = 1
        ): Search

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
        suspend fun popular(
            @Query("page") page: Int = 1
        ): Popular

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v2.2/films/top?type=TOP_250_BEST_FILMS")
        suspend fun getTop(
            @Query("page") page: Int = 1
        ): Top


        @Headers("X-API-KEY: $api_key")
        @GET("/api/v2.2/films/{id}")
        suspend fun getMovieById(
            @Path("id") id: String
        ): Movie

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v2.2/films/{id}/similars")
        suspend fun getSameMovie(
            @Path("id") id: String
        ): Similars

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v2.2/films/{id}/images")
        suspend fun getImagesShort(
            @Path("id") id: String,
            @Query("page") page: Int = 1
        ): Images

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v1/staff/{id}")
        suspend fun getPersonnelDetails(
            @Path("id") id: String
        ): Person

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v1/staff?")
        suspend fun actorsList(
            @Query("filmId") filmId: Int
        ): Workers

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v2.2/films")
        suspend fun getDramaFrance(
            @Query("countries") countries: Int = 3,
            @Query("genres") genres: Int = 2,
            @Query("page") page: Int = 1,
        ): Search

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v2.2/films")
        suspend fun getSeries(
            @Query("type") type: String = "TV_SERIES",
            @Query("page") page: Int = 1,
        ): Search

        @Headers("X-API-KEY: $api_key")
        @GET("/api/v2.2/films")
        suspend fun getSearch(
            @Query("countries") countries: Int? = null,
            @Query("genres") genres: Int? = null,
            @Query("type") type: String = "ALL",
            @Query("order") order: String = "RATING",
            @Query("ratingFrom") ratingFrom: Int = 0,
            @Query("ratingTo") ratingTo: Int = 10,
            @Query("yearFrom") yearFrom: Int = 1950,
            @Query("yearTo") yearTo: Int = 2030,
            @Query("keyword") keyword: String = "",
        ): Search?
    }

}

suspend fun main() {
    coroutineScope {
        launch(Dispatchers.IO) {
            val test =
                NetworkService.cinemaService.getSearch(genres = 11)?.mapToBriefMovieList()
                    ?: mutableListOf()
            for (movie in test) {
                println(movie.toString())
            }
        }
    }
    exitProcess(0)
}
