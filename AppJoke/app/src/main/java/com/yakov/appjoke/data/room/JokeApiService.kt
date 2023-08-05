package com.yakov.appjoke.data.room

import com.yakov.appjoke.data.model.Joke
import retrofit2.http.GET
import retrofit2.http.Query

interface JokeApiService {
    @GET("jokes/random")
    suspend fun getRandomJokeByCategory(@Query("category") category: String): Joke

    @GET("jokes/categories")
    suspend fun getCategories(): List<String>
}