package com.example.charger.data.model.user


import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("EmailAddress") val emailAddress: String,
    @SerializedName("Identifier") val identifier: String,
    @SerializedName("ProfileImageURL") val profileImageURL: String,
    @SerializedName("ID") val iD: Int,
    @SerializedName("Username") val username: String
)