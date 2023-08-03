package com.yakov.cinema.ui.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yakov.cinema.data.repository.Repository
import com.yakov.cinema.data.store.StoreManager
import kotlinx.coroutines.launch

class MainViewModel(val repository: Repository) : ViewModel() {

    private var _welcome = MutableLiveData<Boolean?>()
    var welcome: LiveData<Boolean?> = _welcome


    fun fillWelcome() {
        viewModelScope.launch {
            _welcome.value = repository.getWelcomeByStore()
        }
    }

    fun saveWithoutWelcome() {
        viewModelScope.launch {
            repository.saveWithoutWelcome()
        }
    }

    fun fillDataApp() {
        viewModelScope.launch {
            repository.fillInitialCollections()
        }
    }
}

class MainFactory(context: Context) : ViewModelProvider.Factory {
    private val storeManager = StoreManager(context)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(Repository(storeManager)) as T
    }
}