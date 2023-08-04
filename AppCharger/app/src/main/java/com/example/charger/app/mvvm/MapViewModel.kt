package com.example.charger.app.mvvm

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charger.app.model.Bounds
import com.example.charger.app.model.Coordinates
import com.example.charger.data.model.ChargingLocation
import com.example.charger.data.model.ChargingLocationRequest
import com.example.charger.data.repository.MapRepository
import kotlinx.coroutines.launch

class MapViewModel(var mapRepository: MapRepository) : ViewModel() {

    private val _chargingLocations = MutableLiveData<List<ChargingLocation>>()
    val chargingLocations: LiveData<List<ChargingLocation>> = _chargingLocations

    private val _selectedChargingLocation = MutableLiveData<ChargingLocation>()
    val selectedChargingLocation: LiveData<ChargingLocation> = _selectedChargingLocation

    private val _markerPosition = MutableLiveData<Coordinates>()
    val markerPosition: LiveData<Coordinates> = _markerPosition

    fun saveNewMarkerPosition(lat: Double, lng: Double) {
        _markerPosition.value = Coordinates(lat, lng)
    }

    fun selectById(id: String) {
        val chargingLocations = _chargingLocations.value
        if (chargingLocations != null) {
            for (location in chargingLocations) {
                if (location.id == id.toInt()) {
                    _selectedChargingLocation.value = location
                    break
                }
            }
        }
    }

    fun fetchChargingLocations(bounds: Bounds) {
        viewModelScope.launch {
            val request = ChargingLocationRequest(
                latitude = (bounds.swLat + bounds.neLat) / 2,
                longitude = (bounds.swLon + bounds.neLon) / 2,
                distance = calculateDistanceInKilometers(bounds),
                maxResults = 10
            )
            val chargingLocations = mapRepository.getChargingLocations(request)
            _chargingLocations.value = chargingLocations
        }
    }

    private fun calculateDistanceInKilometers(bounds: Bounds): Int {
        val startLocation = Location("")
        startLocation.latitude = bounds.swLat
        startLocation.longitude = bounds.swLon

        val endLocation = Location("")
        endLocation.latitude = bounds.neLat
        endLocation.longitude = bounds.neLon

        val distanceInMeters = startLocation.distanceTo(endLocation)
        return (distanceInMeters / 1000).toInt()
    }
}

