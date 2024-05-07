package models

data class Cell(
    val coordinates: Coordinates,
    val state: CellState = CellState.UNSELECTED,
    val isMine: Boolean = false,
    val neighbourMines: Int = 0
)