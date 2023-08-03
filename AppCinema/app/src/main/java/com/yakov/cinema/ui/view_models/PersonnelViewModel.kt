package com.yakov.cinema.ui.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yakov.cinema.constants_and_extensions.updateBestMovie
import com.yakov.cinema.data.repository.Repository
import com.yakov.cinema.data.model.app_model.PersonnelDetails
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.data.store.StoreManager
import kotlinx.coroutines.launch

class PersonnelViewModel(val repository: Repository) : ViewModel() {
    private var _personnel = MutableLiveData<Result<PersonnelDetails>>()
    var personnel: LiveData<Result<PersonnelDetails>> = _personnel

    fun updatePersonnel(id: Int) {
        _personnel.value = Result.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getPersonnelDetailsData(id)
                _personnel.value = Result.Success(response)

                response.updateBestMovie()
                _personnel.value = Result.Success(response)
            } catch (e: Exception) {
                _personnel.value = Result.Failure(e)
            }
        }
    }
}

class PersonnelFactory(context: Context) : ViewModelProvider.Factory {
    private val storeManager = StoreManager(context)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PersonnelViewModel(Repository(storeManager)) as T
    }
}