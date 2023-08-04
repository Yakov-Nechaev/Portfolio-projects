package com.example.charger.data.model

import com.google.gson.annotations.SerializedName

data class ChargingLocation(
    @SerializedName("AddressInfo") val addressInfo: AddressInfo,
    @SerializedName("UsageCost") val usageCost: String,
    @SerializedName("OperatorInfo") val operatorInfo: OperatorInfo,
    @SerializedName("Connections") val connections: List<Connection>,
    @SerializedName("DateLastVerified") val dateLastVerified: String,
    @SerializedName("ID") val id: Int
)
