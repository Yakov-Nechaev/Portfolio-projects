package com.example.charger.data.model

data class ChargingLocationRequest(
    val latitude: Double,
    val longitude: Double,
    val distance: Int,
    val maxResults: Int
)