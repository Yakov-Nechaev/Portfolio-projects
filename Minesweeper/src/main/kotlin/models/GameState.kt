package models

enum class GameState(val iconPath: String, val description: String) {
    PLAY(iconPath = "face_default.svg", description = "smiling face"),
    WON(iconPath = "face_won.svg", description = "face with sunglasses"),
    LOST(iconPath = "face_lost.svg", description = "sad face")
}