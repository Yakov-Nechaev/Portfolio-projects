package com.example.charger.data.model.user


import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("UserProfile") val userProfile: UserProfile
)