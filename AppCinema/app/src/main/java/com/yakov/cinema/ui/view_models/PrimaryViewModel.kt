package com.yakov.cinema.ui.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.data.repository.Repository
import com.yakov.cinema.data.repository.Result
import com.yakov.cinema.data.store.StoreManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PrimaryViewModel(val repository: Repository) : ViewModel() {

    private var _stateLoading = MutableLiveData<Boolean>()
    var stateLoading: LiveData<Boolean> = _stateLoading

    private var _listTopResult = MutableLiveData<Result<List<BriefMovie>>>()
    var listTopResult: LiveData<Result<List<BriefMovie>>> = _listTopResult

    private var _listPremieres = MutableLiveData<Result<List<BriefMovie>>>()
    var listPremieres: LiveData<Result<List<BriefMovie>>> = _listPremieres

    private var _listPopular = MutableLiveData<Result<List<BriefMovie>>>()
    var listPopular: LiveData<Result<List<BriefMovie>>> = _listPopular

    private var _listActionUSA = MutableLiveData<Result<List<BriefMovie>>>()
    var listActionUSA: LiveData<Result<List<BriefMovie>>> = _listActionUSA

    private var _listDramFrance = MutableLiveData<Result<List<BriefMovie>>>()
    var listDramFrance: LiveData<Result<List<BriefMovie>>> = _listDramFrance

    private var _listSoap = MutableLiveData<Result<List<BriefMovie>>>()
    var listSoap: LiveData<Result<List<BriefMovie>>> = _listSoap

    fun updateActionUSA() {
        viewModelScope.launch {
            _listActionUSA.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getActionUSAFull(page = 1)
                if (_stateLoading.value == true) _stateLoading.value = false
                _listActionUSA.value = Result.Success(response)

            } catch (e: Exception) {
                _listActionUSA.value = Result.Failure(e)
            }
        }
    }

    fun updateDramFrance() {
        viewModelScope.launch {
            _listDramFrance.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getDramFranceFull(page = 1)
                if (_stateLoading.value == true) _stateLoading.value = false
                _listDramFrance.value = Result.Success(response)

            } catch (e: Exception) {
                _listDramFrance.value = Result.Failure(e)
            }
        }
    }

    fun updateSoap() {
        viewModelScope.launch {
            _listSoap.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getSoapFull(page = 1)
                if (_stateLoading.value == true) _stateLoading.value = false
                _listSoap.value = Result.Success(response)

            } catch (e: Exception) {
                _listSoap.value = Result.Failure(e)
            }
        }
    }

    fun updateTopShort() {
        viewModelScope.launch {
            _listTopResult.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getTopListFull(page = 1)
                if (_stateLoading.value == true) _stateLoading.value = false
                _listTopResult.value = Result.Success(response)

            } catch (e: Exception) {
                _listTopResult.value = Result.Failure(e)
            }
        }
    }

    fun updatePopularShort() {
        viewModelScope.launch {
            _listPopular.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getPopularListFull(page = 1)
                if (_stateLoading.value == true) _stateLoading.value = false
                _listPopular.value = Result.Success(response)
            } catch (e: Exception) {
                _listPopular.value = Result.Failure(e)
            }
        }
    }

    fun updatePremieres() {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
        val month = monthFormat.format(Calendar.getInstance().time)

        viewModelScope.launch {
            _listPremieres.value = Result.Loading()
            if (_stateLoading.value == false || _stateLoading.value == null) _stateLoading.value =
                true
            try {
                val response = repository.getPremieres(year, month, page = 1)
                if (_stateLoading.value == true) _stateLoading.value = false
                _listPremieres.value = Result.Success(response)
            } catch (e: Exception) {
                _listPremieres.value = Result.Failure(e)
            }
        }
    }

    fun saveItemWasInterested(id: Int) {
        viewModelScope.launch {
            repository.saveInWasInterested(id)
        }
    }

}

class PrimaryFactory(context: Context) : ViewModelProvider.Factory {
    private val storeManager = StoreManager(context)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PrimaryViewModel(Repository(storeManager)) as T
    }
}