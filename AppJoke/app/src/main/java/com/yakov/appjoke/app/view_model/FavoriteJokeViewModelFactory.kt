package com.yakov.appjoke.app.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yakov.appjoke.data.repository.DataRepository

class FavoriteJokeViewModelFactory(var repository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteJokeViewModel(repository) as T
    }
}