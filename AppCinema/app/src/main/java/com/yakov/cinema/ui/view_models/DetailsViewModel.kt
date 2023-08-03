package com.yakov.cinema.ui.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yakov.cinema.constants_and_extensions.Constants
import com.yakov.cinema.data.repository.Repository
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.data.model.app_model.CollectionMovie
import com.yakov.cinema.data.model.app_model.Crew
import com.yakov.cinema.data.model.app_model.DetailsMovie
import com.yakov.cinema.data.model.app_model.ImagesMovie
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.data.store.StoreManager
import kotlinx.coroutines.launch

class DetailsViewModel(val repository: Repository) : ViewModel() {

    private var _stateLoading = MutableLiveData<Boolean>()
    var stateLoading: LiveData<Boolean> = _stateLoading

    private var _movie = MutableLiveData<Result<DetailsMovie>>()
    var movie: LiveData<Result<DetailsMovie>> = _movie

    private var _team = MutableLiveData<Result<Crew>>()
    var team: LiveData<Result<Crew>> = _team

    private var _allCollectionsList = MutableLiveData<List<CollectionMovie>>()
    var allCollectionList: LiveData<List<CollectionMovie>> = _allCollectionsList

    private var _similarMovie = MutableLiveData<Result<List<BriefMovie>>>()
    var similarMovie: LiveData<Result<List<BriefMovie>>> = _similarMovie

    private var _images = MutableLiveData<Result<List<ImagesMovie>>>()
    var images: LiveData<Result<List<ImagesMovie>>> = _images

    private var _viewed = MutableLiveData<Boolean>()
    var viewed: LiveData<Boolean> = _viewed

    private var _favorite = MutableLiveData<Boolean>()
    var favorite: LiveData<Boolean> = _favorite

    private var _marc = MutableLiveData<Boolean>()
    var marc: LiveData<Boolean> = _marc


    fun updateMovie(id: Int) {
        viewModelScope.launch {
            _movie.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getMovie(id)
                if (_stateLoading.value == true) _stateLoading.value = false
                _movie.value = Result.Success(response)

            } catch (e: Exception) {
                _movie.value = Result.Failure(e)
            }
        }
    }

    fun updateTeam(id: Int) {
        viewModelScope.launch {
            _team.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getTeam(id)
                if (_stateLoading.value == true) _stateLoading.value = false
                _team.value = Result.Success(response)

            } catch (e: Exception) {
                _team.value = Result.Failure(e)
            }
        }
    }

    fun updateSimilar(id: Int) {
        viewModelScope.launch {
            _similarMovie.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getSimilarMovie(id)
                if (_stateLoading.value == true) _stateLoading.value = false
                _similarMovie.value = Result.Success(response)

            } catch (e: Exception) {
                _similarMovie.value = Result.Failure(e)
            }
        }
    }

    fun updateImages(id: Int) {
        viewModelScope.launch {
            _images.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getImagesShort(id)
                if (_stateLoading.value == true) _stateLoading.value = false
                _images.value = Result.Success(response)

            } catch (e: Exception) {
                _images.value = Result.Failure(e)
            }
        }
    }

    fun addItemInCollection(id: Int, nameCollection: String) {
        viewModelScope.launch {
            repository.addMovieInCollection(nameCollection, id)
            updateAllCollection()
            updateMarc(id)
            updateFavorite(id)
        }
    }

    fun removeItemInCollection(id: Int, nameCollection: String) {
        viewModelScope.launch {
            repository.removeMovieFromCollection(nameCollection, id)
            updateAllCollection()
            updateMarc(id)
            updateFavorite(id)
        }
    }

    fun addNewCollection(name: String) {
        viewModelScope.launch {
            repository.addCollection(name)
            updateAllCollection()
        }
    }

    fun updateAllCollection() {
        viewModelScope.launch {
            _allCollectionsList.value = repository.getCollections()
        }
    }

    fun updateMarc(id: Int) {
        viewModelScope.launch {
            val listMarc: List<String> =
                repository.getCollectionListMovie(Constants.COLLECTION_WANT_TO_SEE)
            _marc.value = listMarc.contains(id.toString())
        }
    }

    fun addMarc(id: Int) {
        viewModelScope.launch {
            repository.addMovieInCollection(name = Constants.COLLECTION_WANT_TO_SEE, id = id)
            updateMarc(id)
            updateAllCollection()
        }
    }

    fun removeMarc(id: Int) {
        viewModelScope.launch {
            repository.removeMovieFromCollection(name = Constants.COLLECTION_WANT_TO_SEE, id = id)
            updateMarc(id)
            updateAllCollection()
        }
    }

    fun updateFavorite(id: Int) {
        viewModelScope.launch {
            val listFavorite: List<String> =
                repository.getCollectionListMovie(Constants.COLLECTION_FAVORITE)
            _favorite.value = listFavorite.contains(id.toString())
        }
    }

    fun addFavorite(id: Int) {
        viewModelScope.launch {
            repository.addMovieInCollection(name = Constants.COLLECTION_FAVORITE, id = id)
            updateFavorite(id)
            updateAllCollection()
        }
    }

    fun removeFavorite(id: Int) {
        viewModelScope.launch {
            repository.removeMovieFromCollection(name = Constants.COLLECTION_FAVORITE, id = id)
            updateFavorite(id)
            updateAllCollection()
        }
    }

    fun updateViewedPorId(id: Int) {
        viewModelScope.launch {
            val listViewed: List<String> = repository.getListViewedId()
            _viewed.value = listViewed.contains(id.toString())
        }
    }

    fun addViewed(id: Int) {
        viewModelScope.launch {
            repository.listViewedAddItem(id)
            updateViewedPorId(id)
        }
    }

    fun removeViewed(id: Int) {
        viewModelScope.launch {
            repository.listViewedRemoveItem(id)
            updateViewedPorId(id)
        }
    }

    fun saveItemWasInterested(id: Int) {
        viewModelScope.launch {
            repository.saveInWasInterested(id)
        }
    }
}

class DetailsFactory(context: Context) : ViewModelProvider.Factory {
    private val storeManager = StoreManager(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(Repository(storeManager)) as T
    }
}