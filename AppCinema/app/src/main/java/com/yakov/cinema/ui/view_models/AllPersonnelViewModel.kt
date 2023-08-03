package com.yakov.cinema.ui.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yakov.cinema.data.repository.Repository
import com.yakov.cinema.data.model.app_model.Crew
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.data.store.StoreManager
import kotlinx.coroutines.launch

class AllPersonnelViewModel(var repository: Repository): ViewModel() {

    private var _team = MutableLiveData<Result<Crew>>()
    var team: LiveData<Result<Crew>> = _team

    fun updateTeam(id: Int) {
        viewModelScope.launch {
            _team.value = Result.Loading()
            try {
                val response = repository.getTeam(id)
                _team.value = Result.Success(response)
            } catch (e: Exception) {
                _team.value = Result.Failure(e)
            }
        }
    }
}

class AllPersonnelFactory(context: Context): ViewModelProvider.Factory {
    private val storeManager = StoreManager(context)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllPersonnelViewModel(Repository(storeManager)) as T
    }
}