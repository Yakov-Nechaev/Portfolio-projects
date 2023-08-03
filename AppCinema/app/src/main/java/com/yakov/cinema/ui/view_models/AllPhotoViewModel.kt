package com.yakov.cinema.ui.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yakov.cinema.data.repository.Repository
import com.yakov.cinema.data.model.app_model.ImagesMovie
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.data.store.StoreManager
import kotlinx.coroutines.launch

class AllPhotoViewModel(val repository: Repository): ViewModel() {

    private var _images = MutableLiveData<Result<List<ImagesMovie>>>()
    var images: LiveData<Result<List<ImagesMovie>>> = _images

    fun updateImages(id: Int) {
        viewModelScope.launch {
            _images.value = Result.Loading()
            try {
                val response = repository.getImagesShort(id)
                _images.value = Result.Success(response)
            } catch (e: Exception) {
                _images.value = Result.Failure(e)
            }
        }
    }
}

class AllPhotoFactory(context: Context): ViewModelProvider.Factory {
    private val storeManager = StoreManager(context)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllPhotoViewModel(Repository(storeManager)) as T
    }
}