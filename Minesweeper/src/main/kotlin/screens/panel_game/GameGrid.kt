package screens.panel_game

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import models.*
import screens.common.drawEdgeNotElevated

@Composable
fun GameGrid(
    level: Level,
    board: List<Cell>,
    gameState: GameState,
    onSelectCell: (Coordinates) -> Unit,
    onFlagCell: (Coordinates) -> Unit,

    ) {
    Column(
        modifier = Modifier
            .background(color = Color.Gray)
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp, top = 10.dp)
            .drawBehind { drawEdgeNotElevated(drawScope = this, 3f) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        repeat(level.rows) { row ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(level.columns) { col ->
                    CellElement(
                        gameState = gameState,
                        cell = board[row * level.columns + col],
                        onSelectedCell = onSelectCell,
                        onFlagCell = onFlagCell
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GridPreviewInitial() {
    val board = MutableList(9) { y ->
        MutableList(9) { x ->
            Cell(coordinates = Coordinates(y, x))
        }
    }.flatten()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GameGrid(
            level = Level.BEGINNER,
            board = board,
            gameState = GameState.PLAY,
            {}, {},
        )
    }
}

@Preview
@Composable
fun GridPreviewAllSelected() {
    val board = MutableList(9) { y ->
        MutableList(9) { x ->
            Cell(coordinates = Coordinates(y, x), state = CellState.SELECTED)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GameGrid(
            level = Level.BEGINNER,
            board = board.flatten(),
            gameState = GameState.PLAY,
            {}, {},
        )
    }
}

@Preview
@Composable
fun GridPreviewGame() {

    val board = MutableList(9) { y ->
        MutableList(9) { x ->
            Cell(coordinates = Coordinates(y, x), state = CellState.SELECTED)
        }
    }

    for (coordinates in listOf(
        Coordinates(1, 6),
        Coordinates(6, 2),
        Coordinates(6, 3),
        Coordinates(6, 4)
    )
    ) {
        board[coordinates.y][coordinates.x] = board[coordinates.y][coordinates.x].copy(state = CellState.FLAGGED)
    }

    for (coordinates in listOf(
        Coordinates(0, 5),
        Coordinates(0, 6),
        Coordinates(0, 7),
        Coordinates(1, 5),
        Coordinates(1, 7),
        Coordinates(2, 5),
        Coordinates(2, 6),
        Coordinates(2, 7),
        Coordinates(6, 1),
        Coordinates(6, 5),
    )) {
        board[coordinates.y][coordinates.x] = board[coordinates.y][coordinates.x].copy(neighbourMines = 1)
    }

    for (coordinates in listOf(
        Coordinates(5, 2),
        Coordinates(5, 4),
        Coordinates(7, 2),
        Coordinates(7, 4),
    )) {
        board[coordinates.y][coordinates.x] = board[coordinates.y][coordinates.x].copy(neighbourMines = 2)
    }

    for (coordinates in listOf(
        Coordinates(5, 3),
        Coordinates(7, 3),
    )) {
        board[coordinates.y][coordinates.x] = board[coordinates.y][coordinates.x].copy(neighbourMines = 3)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GameGrid(
            level = Level.BEGINNER,
            board = board.flatten(),
            gameState = GameState.WON,
            {}, {},
        )
    }
}