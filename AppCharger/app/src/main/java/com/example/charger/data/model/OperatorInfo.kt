package com.example.charger.data.model

import com.google.gson.annotations.SerializedName

data class OperatorInfo(
    @SerializedName("PhonePrimaryContact") val phonePrimaryContact: String?,
    @SerializedName("WebsiteURL") val websiteURL: String?,
    @SerializedName("Title") val title: String
)