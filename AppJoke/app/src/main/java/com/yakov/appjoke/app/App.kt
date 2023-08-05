package com.yakov.appjoke.app

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.yakov.appjoke.data.room.AppDatabase
import com.yakov.appjoke.data.room.JokeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.chucknorris.io/"

class App : Application() {
    lateinit var db: AppDatabase
    val dataStore: DataStore<Preferences> by preferencesDataStore("settings")

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    val jokeApi: JokeApiService =
        retrofit.create(JokeApiService::class.java)

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "db").build()
    }
}