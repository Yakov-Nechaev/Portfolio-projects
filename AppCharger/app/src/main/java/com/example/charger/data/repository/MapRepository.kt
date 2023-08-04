package com.example.charger.data.repository

import com.example.charger.data.NetworkService
import com.example.charger.data.model.ChargingLocation
import com.example.charger.data.model.ChargingLocationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapRepository(val ocmApiService: NetworkService.OcmApiService) {

    suspend fun getChargingLocations(request: ChargingLocationRequest): List<ChargingLocation> {
        return withContext(Dispatchers.IO) {
            ocmApiService.getChargingLocations(
                latitude = request.latitude,
                longitude = request.longitude,
                distance = request.distance,
                maxResults = request.maxResults,
            )
        }
    }

    suspend fun getCharging(id: String) : List<ChargingLocation> {
        return withContext(Dispatchers.IO) {
            ocmApiService.getChargingLocationById(id)
        }
    }
}