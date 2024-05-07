import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import models.Level
import mvvm.GameViewModel
import screens.screen_principal.GameScreen

fun main() = application {

    val gameViewModel = GameViewModel()

    val windowState = rememberWindowState(
        size = DpSize(
            width = (20.dp * gameViewModel.level.value.columns) + 45.dp,
            height = (20.dp * gameViewModel.level.value.rows) + 157.dp
        ),
        position = WindowPosition(Alignment.Center)
    )

    LaunchedEffect(Unit) {
        gameViewModel.reset()

        gameViewModel.level.collect {
            windowState.apply {
                size = DpSize(
                    width = (20.dp * gameViewModel.level.value.columns) + 45.dp,
                    height = (20.dp * gameViewModel.level.value.rows) + 157.dp
                )
                position = WindowPosition(Alignment.Center)
            }
        }
    }

    Window(
        onCloseRequest = { exitApplication() },
        state = windowState,
        title = "Minesweeper",
        icon = painterResource("mine.svg"),
        resizable = false,
    ) {
        MenuBar {
            Menu(text = "Options") {
                Item(text = "BEGINNER", mnemonic = 'B') { gameViewModel.changeLevel(Level.BEGINNER) }
                Item(text = "INTERMEDIATE", mnemonic = 'I') { gameViewModel.changeLevel(Level.INTERMEDIATE) }
                Item(text = "EXPERT", mnemonic = 'E') { gameViewModel.changeLevel(Level.EXPERT) }
            }
        }
        GameScreen(gameViewModel)
    }
}