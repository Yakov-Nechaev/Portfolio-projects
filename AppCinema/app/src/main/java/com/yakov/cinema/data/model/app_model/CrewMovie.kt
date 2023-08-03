package com.yakov.cinema.data.model.app_model

data class Crew(
    var actorList: MutableList<Actor>,
    var personnelList: MutableList<Personnel>
)

data class Actor(
    val id: Int,
    val name: String,
    val role: String,
    val image: String
) : Team {
    override fun id(): Int = id
}

data class Personnel(
    val id: Int,
    val name: String,
    val role: String,
    val image: String
) : Team {
    override fun id(): Int = id
}

interface Team {
    fun id(): Int
}
