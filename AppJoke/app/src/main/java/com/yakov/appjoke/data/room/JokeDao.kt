package com.yakov.appjoke.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yakov.appjoke.data.model.Joke
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {
    @Query("SELECT * FROM jokes")
    fun getList(): Flow<MutableList<Joke>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addJoke(joke: Joke)

    @Delete
    suspend fun removeJoke(joke: Joke)
}