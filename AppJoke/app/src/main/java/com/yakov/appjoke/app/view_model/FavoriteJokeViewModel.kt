package com.yakov.appjoke.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakov.appjoke.data.model.Joke
import com.yakov.appjoke.data.repository.DataRepository
import kotlinx.coroutines.launch

class FavoriteJokeViewModel(private val repository: DataRepository) : ViewModel() {

    private val _listJoke: MutableLiveData<MutableList<Joke>> = MutableLiveData()
    val listJoke: LiveData<MutableList<Joke>> = _listJoke

    fun fillListJoke () {
        viewModelScope.launch {
            repository.getList().collect { list ->
                _listJoke.value = list
            }
        }
    }

    fun removeJoke(joke: Joke) {
        viewModelScope.launch {
            repository.removeJoke(joke)
        }
    }
}