package com.example.mysn

data class Coordinates(
    val x: Int,
    val y: Int
)

fun Coordinates.mapToString(): String {
    return when {
        this == Coordinates(0, -1) -> "↑"   // alt + 24
        this == Coordinates(0, 1) -> "↓"    // alt + 25
        this == Coordinates(1, 0) -> "→"    // alt + 26
        this == Coordinates(-1, 0) -> "←"   // alt + 27
        else -> ""
    }
}