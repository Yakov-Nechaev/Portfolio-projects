package com.yakov.cinema.data.model.app_model

data class SearchParams(
    var countries: Int? = null,
    var genres: Int? = null,
    var type: String = "ALL",
    var order: String = "RATING",
    var ratingFrom: Int = 0,
    var ratingTo: Int = 10,
    var yearFrom: Int = 1950,
    var yearTo: Int = 2023,
    var keyword: String = "",
)