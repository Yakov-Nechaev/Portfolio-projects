package com.yakov.cinema.ui.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yakov.cinema.data.repository.Repository
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.data.model.app_model.CollectionMovie
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.data.store.StoreManager
import kotlinx.coroutines.launch

class ProfileViewModel(var repository: Repository) : ViewModel() {

    private var _welcome = MutableLiveData<Boolean>()
    var welcome: LiveData<Boolean> = _welcome

    private var _stateLoading = MutableLiveData<Boolean>()
    var stateLoading: LiveData<Boolean> = _stateLoading

    private var _listWasInterested = MutableLiveData<Result<List<BriefMovie>>>()
    var listWasInterested: LiveData<Result<List<BriefMovie>>> = _listWasInterested

    private var _listViewed = MutableLiveData<Result<List<BriefMovie>>>()
    var listViewed: LiveData<Result<List<BriefMovie>>> = _listViewed

    private var _listCollection = MutableLiveData<List<CollectionMovie>>()
    var listCollection: LiveData<List<CollectionMovie>> = _listCollection

    private var _listMovieFromCollection = MutableLiveData<Result<List<BriefMovie>>>()
    var listMovieFromCollection: LiveData<Result<List<BriefMovie>>> = _listMovieFromCollection


    fun updateWelcome() {
        viewModelScope.launch {
            _welcome.value = repository.getWelcomeByStore() ?: false
        }
    }

    fun saveWithWelcome() {
        viewModelScope.launch {
            repository.saveWithWelcome()
        }
    }

    fun saveWithoutWelcome() {
        viewModelScope.launch {
            repository.saveWithoutWelcome()
        }
    }

    fun updateListMovieFromCollection(nameCollection: String) {
        viewModelScope.launch {
            _listMovieFromCollection.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getDetailsMovieFromCollection(nameCollection)
                if (_stateLoading.value == true) _stateLoading.value = false
                _listMovieFromCollection.value = Result.Success(response)
            } catch (e: Exception) {
                _listMovieFromCollection.value = Result.Failure(e)
            }
        }
    }

    fun deleteCollection(name: String) {
        viewModelScope.launch {
            repository.deleteCollection(name)
            fillListCollection()
        }
    }

    fun addNewCollection(name: String) {
        viewModelScope.launch {
            repository.addCollection(name)
            fillListCollection()
        }
    }

    fun fillListCollection() {
        viewModelScope.launch {
            _listCollection.value = repository.getCollections()
        }
    }

    fun updateWasInterested() {
        viewModelScope.launch {
            _listWasInterested.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getListMovieWasInterested()
                if (_stateLoading.value == true) _stateLoading.value = false
                _listWasInterested.value = Result.Success(response)
            } catch (e: Exception) {
                _listWasInterested.value = Result.Failure(e)
            }
        }
    }

    fun clearDataWasInterested() {
        viewModelScope.launch {
            repository.clearWasInterested()
            _listWasInterested.value = Result.Success(data = emptyList())
        }
    }

    fun updateViewed() {
        viewModelScope.launch {
            _listViewed.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getListMovieWasViewed()
                if (_stateLoading.value == true) _stateLoading.value = false
                _listViewed.value = Result.Success(response)
            } catch (e: Exception) {
                _listViewed.value = Result.Failure(e)
            }
        }
    }

    fun clearDataViewed() {
        viewModelScope.launch {
            repository.clearAllViewed()
            _listViewed.value = Result.Success(data = emptyList())
        }
    }

}

class ProfileFactory(context: Context) : ViewModelProvider.Factory {
    private val storeManager = StoreManager(context)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(Repository(storeManager)) as T
    }
}