package com.yakov.cinema.ui.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yakov.cinema.data.repository.Repository
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.data.model.app_model.SearchParams
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.data.store.StoreManager
import kotlinx.coroutines.launch

class SearchViewModel(val repository: Repository) : ViewModel() {

    private var _searchParams = MutableLiveData(SearchParams())

    private var _listMovies = MutableLiveData<Result<MutableList<BriefMovie>>>()
    var listMovie: LiveData<Result<MutableList<BriefMovie>>> = _listMovies

    fun getSearch() {
        viewModelScope.launch {
            _listMovies.value = Result.Loading()
            try {
                _searchParams.value?.let {
                    _listMovies.value = Result.Success(data = repository.getListBySearch(it))
                }
            } catch (e: Exception) {
                _listMovies.value = Result.Failure(e)
            }
        }
    }

    fun setKeyword(keyWord: String) {
        _searchParams.value?.keyword = keyWord
    }

    fun setType(type: String) {
        _searchParams.value?.type = type
    }

    fun setCountry(country: Int) {
        if (country == -1) _searchParams.value?.countries = null
        else _searchParams.value?.countries = country
    }

    fun setGenre(genre: Int) {
        if (genre == -1) _searchParams.value?.genres = null
        else _searchParams.value?.genres = genre
    }

    fun setYearFrom(yearFrom: Int) {
        _searchParams.value?.yearFrom = yearFrom
    }

    fun setYearTo(yearTo: Int) {
        _searchParams.value?.yearTo = yearTo
    }

    fun setRatingFrom(ratingFrom: Int) {
        _searchParams.value?.ratingFrom = ratingFrom
    }

    fun setRatingTo(ratingTo: Int) {
        _searchParams.value?.ratingTo = ratingTo
    }

    fun setOrder(order: String) {
        _searchParams.value?.order = order
    }

    fun saveItemWasInterested(id: Int) {
        viewModelScope.launch {
            repository.saveInWasInterested(id)
        }
    }
}

class SearchFactory(context: Context) :  ViewModelProvider.Factory {
    private val storeManager = StoreManager(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(Repository(storeManager)) as T
    }
}