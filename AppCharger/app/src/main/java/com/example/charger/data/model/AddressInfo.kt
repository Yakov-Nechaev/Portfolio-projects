package com.example.charger.data.model

import com.google.gson.annotations.SerializedName

data class AddressInfo(
    @SerializedName("Title") val title: String,
    @SerializedName("AddressLine1") val addressLine1: String,
    @SerializedName("StateOrProvince") val stateOrProvince: String,
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Longitude") val longitude: Double,
)