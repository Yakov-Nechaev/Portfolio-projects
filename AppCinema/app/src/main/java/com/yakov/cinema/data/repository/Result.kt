package com.yakov.cinema.data.repository

sealed class Result<out T> {
    class Loading<Nothing> : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
}