package com.yakov.appjoke.data.repository

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yakov.appjoke.app.App
import com.yakov.appjoke.data.model.Category
import com.yakov.appjoke.data.model.Joke
import com.yakov.appjoke.data.room.JokeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

const val KEY_INTERVAL = "interval"
const val KEY_CATEGORY = "category"
const val UPDATE_INTERVAL = 60_000L

class DataRepository(val app: App, val jokeApi: JokeApiService) {

    private var lastUpdateTime: Long = 0

    suspend fun getCategories(): List<Category> {
        val currentTime = System.currentTimeMillis()

        return if (currentTime - lastUpdateTime >= UPDATE_INTERVAL) {
            try {
                val categories = withContext(Dispatchers.IO) {
                    getCategoriesFromNetwork()
                }
                saveCategoriesToDb(categories)
                lastUpdateTime = currentTime
                categories
            } catch (e: Exception) {
                getCategoriesFromDB()
            }
        } else {
            getCategoriesFromDB()
        }
    }

    private suspend fun getCategoriesFromNetwork(): List<Category> {
        val categories = jokeApi.getCategories()
        return categories.map { Category(it) }
    }

    private suspend fun getCategoriesFromDB(): List<Category> {
        return withContext(Dispatchers.IO) {
            app.db.categoryDao().getAll()
        }
    }

    private suspend fun saveCategoriesToDb(categories: List<Category>) {
        withContext(Dispatchers.IO) {
            app.db.categoryDao().insertAll(categories)
        }
    }

    suspend fun getJokeByCategory(keyword: String): Joke {
        return withContext(Dispatchers.IO) {
            jokeApi.getRandomJokeByCategory(keyword)
        }
    }

    suspend fun saveCategoryParam(category: Category) {
        app.dataStore.edit { it[stringPreferencesKey(KEY_CATEGORY)] = category.name }
    }

    suspend fun getCategoryParam(): Category {
        val name = app.dataStore.data.first()[stringPreferencesKey(KEY_CATEGORY)] ?: ""
        return Category(name)
    }

    suspend fun saveIntervalParam(time: Int) {
        app.dataStore.edit { it[intPreferencesKey(KEY_INTERVAL)] = time }
    }

    suspend fun getIntervalParam(): Int {
        return app.dataStore.data.first()[intPreferencesKey(KEY_INTERVAL)] ?: 0
    }

    fun getList(): Flow<MutableList<Joke>> {
        return app.db.jokeDao().getList()
            .flowOn(Dispatchers.IO)
    }

    suspend fun removeJoke(joke: Joke) {
        withContext(Dispatchers.IO) {
            app.db.jokeDao().removeJoke(joke)
        }
    }
}