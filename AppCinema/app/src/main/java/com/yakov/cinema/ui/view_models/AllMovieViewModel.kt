package com.yakov.cinema.ui.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yakov.cinema.data.repository.Repository
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.data.store.StoreManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AllMovieViewModel(val repository: Repository) : ViewModel() {

    private var currentPage = 1
    private var currentList = listOf<BriefMovie>()

    private var _allMovieList = MutableLiveData<Result<List<BriefMovie>>>()
    var allMovieList: LiveData<Result<List<BriefMovie>>> = _allMovieList

    private var _stateProgressLoading = MutableLiveData<Boolean>()
    var stateProgressLoading: LiveData<Boolean> = _stateProgressLoading

    private var _resume = MutableLiveData(true)

    fun fillData(type: String) {
        if (_resume.value == true) {

            if (currentPage == 1) _allMovieList.value = Result.Loading()

            when (type) {
                Constants.SHOW_ALL_POPULAR -> updatePopular()
                Constants.SHOW_ALL_PREMIERES -> updatePremieres()
                Constants.SHOW_ALL_ACTION_USA -> updateActionUSA()
                Constants.SHOW_ALL_TOP_250 -> updateTop250()
                Constants.SHOW_ALL_DRAMA_FRANCE -> updateDramaFrance()
                Constants.SHOW_ALL_SOAP -> updateSoap()
            }
            currentPage++
        }
    }

    private fun updateActionUSA() {
        viewModelScope.launch {
            _stateProgressLoading.value = true
            try {
                val response = repository.getActionUSAFull(currentPage)
                if (response.isEmpty()) _resume.value = false

                currentList = (_allMovieList.value as? Result.Success)?.data ?: emptyList()
                val newList = (currentList + response)

                _allMovieList.value = Result.Success(newList)
                _stateProgressLoading.value = false


            } catch (e: Exception) {
                _allMovieList.value = Result.Failure(e)
            }
        }
    }

    private fun updatePopular() {
        viewModelScope.launch {
            _stateProgressLoading.value = true
            try {
                val response = repository.getPopularListFull(currentPage)
                _resume.value = false
                currentList = (_allMovieList.value as? Result.Success)?.data ?: emptyList()
                val newList = (currentList + response)

                _allMovieList.value = Result.Success(newList)
                _stateProgressLoading.value = false


            } catch (e: Exception) {
                _allMovieList.value = Result.Failure(e)
            }
        }
    }

    private fun updatePremieres() {
        viewModelScope.launch {
            _stateProgressLoading.value = true

            try {
                val year = Calendar.getInstance().get(Calendar.YEAR)
                val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
                val month = monthFormat.format(Calendar.getInstance().time)

                val response = repository.getPremieres(year, month, currentPage)
                _resume.value = false
                currentList = (_allMovieList.value as? Result.Success)?.data ?: emptyList()
                val newList = (currentList + response)

                _allMovieList.value = Result.Success(newList)
                _stateProgressLoading.value = false


            } catch (e: Exception) {
                _allMovieList.value = Result.Failure(e)
            }
        }
    }

    private fun updateTop250() {
        viewModelScope.launch {
            _stateProgressLoading.value = true
            try {
                val response = repository.getTopListFull(currentPage)
                if (response.isEmpty()) _resume.value = false


                currentList = (_allMovieList.value as? Result.Success)?.data ?: emptyList()
                val newList = (currentList + response)

                _allMovieList.value = Result.Success(newList)
                _stateProgressLoading.value = false

            } catch (e: Exception) {
                _allMovieList.value = Result.Failure(e)
            }
        }
    }

    private fun updateDramaFrance() {
        viewModelScope.launch {
            _stateProgressLoading.value = true
            try {
                val response = repository.getDramFranceFull(currentPage)
                if (response.isEmpty()) _resume.value = false
                currentList = (_allMovieList.value as? Result.Success)?.data ?: emptyList()
                val newList = (currentList + response)

                _allMovieList.value = Result.Success(newList)
                _stateProgressLoading.value = false

            } catch (e: Exception) {
                _allMovieList.value = Result.Failure(e)
            }
        }
    }

    private fun updateSoap() {
        viewModelScope.launch {
            _stateProgressLoading.value = true
            try {
                val response = repository.getSoapFull(currentPage)
                if (response.isEmpty()) _resume.value = false
                currentList = (_allMovieList.value as? Result.Success)?.data ?: emptyList()
                val newList = (currentList + response)

                _allMovieList.value = Result.Success(newList)
                _stateProgressLoading.value = false

            } catch (e: Exception) {
                _allMovieList.value = Result.Failure(e)
            }
        }
    }
}

class AllMovieFactory(context: Context) : ViewModelProvider.Factory {
    private val storeManager = StoreManager(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllMovieViewModel(Repository(storeManager)) as T
    }
}