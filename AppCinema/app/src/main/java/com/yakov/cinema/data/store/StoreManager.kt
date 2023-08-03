package com.yakov.cinema.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yakov.cinema.constants_and_extensions.Constants
import kotlinx.coroutines.flow.first


val Context.dataStore: DataStore<Preferences> by preferencesDataStore("data_store")

class StoreManager(val context: Context) {

    suspend fun saveWasInterested(movieId: Int) {
        context.dataStore.edit { preferences ->
            val wasInterestedMovies = preferences[wasInterestedKey] ?: emptySet()
            preferences[wasInterestedKey] = wasInterestedMovies + movieId.toString()
        }
    }

    suspend fun getWasInterested(): List<String> {
        return context.dataStore.data.first()[wasInterestedKey]?.toList() ?: emptyList()
    }

    suspend fun clearWasInterested() {
        context.dataStore.edit { preferences ->
            preferences[wasInterestedKey] = emptySet()
        }
    }


    suspend fun getWasViewed(): List<String> {
        return context.dataStore.data.first()[wasViewedKey]?.toList() ?: emptyList()
    }

    suspend fun clearAllWasViewed() {
        context.dataStore.edit { preferences ->
            preferences[wasViewedKey] = emptySet()
        }
    }

    suspend fun saveWasViewed(movieId: Int) {
        context.dataStore.edit { preferences ->
            val wasViewedMovies = preferences[wasViewedKey] ?: emptySet()
            preferences[wasViewedKey] = wasViewedMovies + movieId.toString()
        }
    }

    suspend fun deleteItemWasViewed(id: Int) {
        context.dataStore.edit { preferences ->
            val wasViewedMovies = preferences[wasViewedKey] ?: emptySet()
            preferences[wasViewedKey] = wasViewedMovies.minus(id.toString())
        }
    }

    suspend fun setDefaultCollection() {
        context.dataStore.edit { preferences ->
            val currentCollection = preferences[collectionKey] ?: emptySet()
            if (currentCollection.isEmpty()) {
                preferences[collectionKey] =
                    setOf(Constants.COLLECTION_FAVORITE, Constants.COLLECTION_WANT_TO_SEE)
            }
        }
    }

    suspend fun getMovieListFromCollection(collectionName: String): List<String> {
        val key = stringSetPreferencesKey(collectionName)
        return context.dataStore.data.first()[key]?.toList() ?: emptyList()
    }

    suspend fun addItemMovieInCollection(collectionName: String, id: Int) {
        val key = stringSetPreferencesKey(collectionName)
        context.dataStore.edit { preferences ->
            val collectionMovie = preferences[key] ?: emptySet()
            preferences[key] = collectionMovie + id.toString()
        }
    }

    suspend fun removeItemMovieFromCollection(collectionName: String, id: Int) {
        val key = stringSetPreferencesKey(collectionName)
        context.dataStore.edit { preferences ->
            val collectionMovie = preferences[key] ?: emptySet()
            preferences[key] = collectionMovie.minus(id.toString())
        }
    }

    suspend fun getCollectionsList(): List<String> {
        return context.dataStore.data.first()[collectionKey]?.toList() ?: emptyList()
    }

    suspend fun addCollection(name: String) {
        context.dataStore.edit { preferences ->
            val collection = preferences[collectionKey] ?: emptySet()
            preferences[collectionKey] = collection + name
        }
    }

    suspend fun deleteItemCollection(name: String) {
        context.dataStore.edit { preferences ->
            val collection = preferences[collectionKey] ?: emptySet()
            preferences[collectionKey] = collection.minus(name)
        }
    }

    suspend fun getWelcome(): Boolean? {
        return context.dataStore.data.first()[welcomeScreenKey]
    }

    suspend fun withWelcome() {
        context.dataStore.edit { preferences ->
            preferences[welcomeScreenKey] = true
        }
    }

    suspend fun withoutWelcome() {
        context.dataStore.edit { preferences ->
            preferences[welcomeScreenKey] = false
        }
    }


    companion object {
        private val collectionKey = stringSetPreferencesKey("collection")
        private val wasInterestedKey = stringSetPreferencesKey("was_interested")
        private val wasViewedKey = stringSetPreferencesKey("was_viewed")
        private val welcomeScreenKey = booleanPreferencesKey("welcome")
    }
}