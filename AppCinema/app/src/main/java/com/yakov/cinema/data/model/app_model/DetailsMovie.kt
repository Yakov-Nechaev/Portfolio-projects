package com.yakov.cinema.data.model.app_model

data class DetailsMovie(
    val id: Int,
    var rating: Double?,
    val name: String,
    val year: Int,
    val genre: String,
    val country: String,
    val lengthMin: Int,
    val shortDescription: String?,
    val completeDescription: String,
    val image: String
) {
    val primaryLine: String
        get() {
            return "${rating.toString()} $name"
        }

    val secondaryLine: String
        get() {
            return "$year, $genre"
        }

    val thirdLine: String
        get() {
            val hours = lengthMin / 60
            val minutes = lengthMin % 60
            val lengthString = String.format("%d ч %02d мин", hours, minutes)
            return "$country, $lengthString"
        }
}
