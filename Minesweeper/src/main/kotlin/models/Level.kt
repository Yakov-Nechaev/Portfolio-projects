package models

enum class Level(val rows: Int, val columns: Int, val mines: Int) {

    BEGINNER(rows = 9, columns = 9, mines = 10),
    INTERMEDIATE(rows = 16, columns = 16, mines = 40),
    EXPERT(rows = 16, columns = 30, mines = 99)

}