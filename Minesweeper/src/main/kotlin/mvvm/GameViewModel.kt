package mvvm

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import models.*

class GameViewModel {

    private var jobTimer = CoroutineScope(Dispatchers.Default)
    val level = MutableStateFlow(Level.INTERMEDIATE)
    val gameState = MutableStateFlow(GameState.PLAY)
    val counterMines = MutableStateFlow(Level.INTERMEDIATE.mines)
    val timer = MutableStateFlow(0)
    private var unselectedCount = level.value.rows * level.value.columns

    private var board =
        MutableList(level.value.rows) { row ->
            MutableList(level.value.columns) { col ->
                Cell(Coordinates(row, col))
            }
        }
    val listOfCell = MutableStateFlow(board.flatten())

    private fun startTimer() {
        jobTimer.launch {
            while (timer.value < 999) {
                timer.value++
                delay(1000)
            }
            gameState.value = GameState.LOST
            cancel()
        }
    }

    fun reset() {
        jobTimer.cancel()
        timer.value = 0
        jobTimer = CoroutineScope(Dispatchers.Default)

        counterMines.value = level.value.mines
        unselectedCount = level.value.rows * level.value.columns

        board = MutableList(level.value.rows) { row ->
            MutableList(level.value.columns) { col ->
                Cell(Coordinates(row, col))
            }
        }

        generateMine()
        listOfCell.value = board.flatten()
        gameState.value = GameState.PLAY
    }

    private fun generateMine() {

        val mines = (0 until level.value.rows * level.value.columns).shuffled().take(level.value.mines)

        for (position in mines) {
            val row = position / level.value.columns
            val col = position % level.value.columns

            board[row][col] = board[row][col].copy(isMine = true)
            generateNeighbourCount(Coordinates(row, col))
        }
    }

    private fun generateNeighbourCount(coordinates: Coordinates) {
        for ((row, col) in getNeighboursWithCheck(coordinates)) {
            val neighbour = board[row][col]
            board[row][col] = neighbour.copy(neighbourMines = neighbour.neighbourMines + 1)
        }
    }

    fun changeLevel(newLevel: Level) {
        level.value = newLevel
        reset()
    }

    fun flagCell(coordinates: Coordinates) {

        if (timer.value == 0) {
            startTimer()
        }

        if (gameState.value == GameState.PLAY) {
            val cell = board[coordinates.y][coordinates.x]
            when (cell.state) {
                CellState.UNSELECTED -> {
                    if (counterMines.value == 0) {
                        return
                    }
                    counterMines.value--
                    board[coordinates.y][coordinates.x] = cell.copy(state = CellState.FLAGGED)
                    listOfCell.value = board.flatten()
                }

                CellState.FLAGGED -> {
                    counterMines.value++
                    board[coordinates.y][coordinates.x] =
                        cell.copy(state = CellState.UNSELECTED)
                    listOfCell.value = board.flatten()
                }

                CellState.SELECTED -> {}
            }
        }
    }

    fun selectedCell(coordinates: Coordinates) {

        if (timer.value == 0) {
            startTimer()
        }

        val cell = board[coordinates.y][coordinates.x]

        if (cell.state == CellState.UNSELECTED && gameState.value == GameState.PLAY) {

            board[coordinates.y][coordinates.x] = cell.copy(state = CellState.SELECTED)
            unselectedCount--

            if (cell.isMine) {
                gameState.value = GameState.LOST
                jobTimer.cancel()
            } else {
                if (cell.neighbourMines == 0) {
                    expandSelection(coordinates)
                }
            }
        } else if (cell.state == CellState.SELECTED && gameState.value == GameState.PLAY) {
            getNeighboursWithoutCheck(cell).forEach { selectedCell(it) }
        }

        if (unselectedCount == level.value.mines) {
            gameState.value = GameState.WON
            counterMines.value = 0
            jobTimer.cancel()
        }

        listOfCell.value = board.flatten()
    }


    // A recursive function that retrieves neighboring cells and checks for the possibility of restarting to expand the game field
    private fun expandSelection(coordinates: Coordinates) {
        getNeighboursWithCheck(coordinates).forEach { (y, x) ->
            val neighbour = board[y][x]

            if (neighbour.state == CellState.UNSELECTED && !neighbour.isMine) {
                board[y][x] = neighbour.copy(state = CellState.SELECTED)
                unselectedCount--
                listOfCell.value = board.flatten()
                if (neighbour.neighbourMines == 0) {
                    expandSelection(neighbour.coordinates)
                }
            }
        }
    }


    // This function retrieves neighboring cells considering mines, in order to perform the expansion of the selected area,
    // i.e., without the possibility of losing.
    private fun getNeighboursWithCheck(coordinates: Coordinates): List<Coordinates> {
        val (row, col) = coordinates
        val neighbours = mutableListOf<Coordinates>()

        for (x in (col - 1)..(col + 1)) {
            for (y in (row - 1)..(row + 1)) {
                if (y in 0 until level.value.rows && x in 0 until level.value.columns) {
                    val neighbour = board[y][x]
                    if (neighbour.state == CellState.UNSELECTED && !neighbour.isMine) {
                        neighbours.add(neighbour.coordinates)
                    }
                }
            }
        }
        neighbours.remove(Coordinates(row, col))
        return neighbours
    }


    // This function retrieves neighboring cells without considering mines,
    // i.e., for a second click on an open cell, with the possibility of losing.
    private fun getNeighboursWithoutCheck(cell: Cell): List<Coordinates> {
        val (row, col) = cell.coordinates

        val flagged = mutableListOf<Coordinates>()
        val unselected = mutableListOf<Coordinates>()

        for (x in (col - 1)..(col + 1)) {
            for (y in (row - 1)..(row + 1)) {
                if (y in 0 until level.value.rows && x in 0 until level.value.columns) {
                    val neighbour = board[y][x]
                    if (neighbour.state == CellState.FLAGGED) {
                        flagged.add(neighbour.coordinates)
                    } else if (neighbour.state == CellState.UNSELECTED) {
                        unselected.add(neighbour.coordinates)
                    }
                }
            }
        }

        return if (flagged.size == cell.neighbourMines) unselected else emptyList()
    }
}