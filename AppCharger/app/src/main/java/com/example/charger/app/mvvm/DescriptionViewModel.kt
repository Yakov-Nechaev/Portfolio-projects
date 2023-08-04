package com.example.charger.app.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charger.data.model.ChargingLocation
import com.example.charger.data.repository.MapRepository
import kotlinx.coroutines.launch

class DescriptionViewModel(val mapRepository: MapRepository) : ViewModel() {

    private val _selectedCharging = MutableLiveData<ChargingLocation>()
    val selectedCharging: LiveData<ChargingLocation> = _selectedCharging

    fun findCharging(id: String) {
        viewModelScope.launch {
            _selectedCharging.value = mapRepository.getCharging(id).first()
        }
    }
}