package com.yakov.cinema.data.model.network_model

import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("items") val items: List<ItemImages>,
    @SerializedName("total") val total: Int,
    @SerializedName("totalPages") val totalPages: Int
)

data class ItemImages(
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("previewUrl") val previewUrl: String
)

