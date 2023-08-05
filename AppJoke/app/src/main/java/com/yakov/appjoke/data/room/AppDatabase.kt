package com.yakov.appjoke.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yakov.appjoke.data.model.Category
import com.yakov.appjoke.data.model.Joke

@Database(entities = [Joke::class, Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jokeDao(): JokeDao
    abstract fun categoryDao(): CategoryDao
}