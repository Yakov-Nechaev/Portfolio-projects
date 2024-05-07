package screens.screen_principal

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import models.Cell
import models.Coordinates
import models.GameState
import models.Level
import mvvm.GameViewModel
import screens.panel_game.GameGrid
import screens.panel_header.PanelHeaderLayout

@Composable
fun GameScreen(gameViewModel: GameViewModel) {

    val level = gameViewModel.level.collectAsState()
    val state = gameViewModel.gameState.collectAsState()
    val counterMines = gameViewModel.counterMines.collectAsState()
    val timer = gameViewModel.timer.collectAsState()
    val listCell = gameViewModel.listOfCell.collectAsState()

    GameScreenLayout(
        flagsRemaining = counterMines.value,
        seconds = timer.value,
        gameState = state.value,
        onResetRequest = { gameViewModel.reset() },
        level = level.value,
        board = listCell.value,
        onSelectCell = { gameViewModel.selectedCell(it) },
        onFlagCell = { gameViewModel.flagCell(it) }
    )
}

@Composable
fun GameScreenLayout(
    flagsRemaining: Int,
    seconds: Int,
    gameState: GameState,
    onResetRequest: () -> Unit,
    level: Level,
    board: List<Cell>,
    onSelectCell: (Coordinates) -> Unit,
    onFlagCell: (Coordinates) -> Unit,
) {
    Column(
        modifier = Modifier.width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PanelHeaderLayout(
            flagsRemaining = flagsRemaining,
            seconds = seconds,
            gameState = gameState,
            onResetRequest = onResetRequest
        )

        GameGrid(
            level = level,
            board = board,
            gameState = gameState,
            onSelectCell = onSelectCell,
            onFlagCell = onFlagCell
        )
    }
}

@Preview
@Composable
fun GamePreview() {
    val gameViewModel = GameViewModel()
    GameScreen(gameViewModel)
}