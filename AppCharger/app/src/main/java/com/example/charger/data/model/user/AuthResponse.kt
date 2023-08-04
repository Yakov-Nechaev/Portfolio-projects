package com.example.charger.data.model.user


import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("Data") val userData: UserData
)