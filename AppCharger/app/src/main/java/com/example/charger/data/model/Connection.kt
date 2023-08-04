package com.example.charger.data.model


import com.google.gson.annotations.SerializedName

data class Connection(
    @SerializedName("PowerKW") val powerKW: Double?
)