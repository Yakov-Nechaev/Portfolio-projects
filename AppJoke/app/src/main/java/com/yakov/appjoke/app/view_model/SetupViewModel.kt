package com.yakov.appjoke.app.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakov.appjoke.data.model.Category
import com.yakov.appjoke.data.repository.DataRepository
import com.yakov.appjoke.work.JokesUpdateWorker
import kotlinx.coroutines.launch

class SetupViewModel(val repository: DataRepository) :
    ViewModel() {

    private val _allCategories = MutableLiveData<List<Category>>()
    val allCategories: LiveData<List<Category>> = _allCategories

    private val _jokeCategory = MutableLiveData<Category>()
    val jokeCategory = _jokeCategory

    private val _timeInterval = MutableLiveData<Int>()
    val timeInterval = _timeInterval

    fun hydrate() {
        viewModelScope.launch {
            _allCategories.value = repository.getCategories()
            _timeInterval.value = repository.getIntervalParam()
            _jokeCategory.value = repository.getCategoryParam()
        }
    }

    fun updateInterval(interval: Int) {
            _timeInterval.value = interval
    }

    fun updateJokeCategory(category: Category) {
            _jokeCategory.value = category
    }

    fun saveData(context: Context) {
        viewModelScope.launch {
            if (_timeInterval.value != null) repository.saveIntervalParam(_timeInterval.value!!)
            if (_jokeCategory.value != null) repository.saveCategoryParam(_jokeCategory.value!!)
        }

        if (_timeInterval.value != null && _jokeCategory.value != null) {
            JokesUpdateWorker.enqueueWork(context)
        }
    }
}