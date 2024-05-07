package panels

import TetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.Coordinates
import models.State
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.event.KeyEvent
import javax.swing.JPanel

class GamePanel(private val viewModel: TetViewModel) : JPanel() {

    init {
        preferredSize = Dimension(300, 600)
        background = Color.LIGHT_GRAY
        isFocusable = true

        CoroutineScope(Dispatchers.Default).launch {
            launch {
                viewModel.pieceCurrent.collect {
                    repaint()
                }
            }
            launch {
                viewModel.state.collect {
                    repaint()
                }
            }
        }
    }

    override fun processKeyEvent(keyEvent: KeyEvent?) {
        super.processKeyEvent(keyEvent)
        if (keyEvent?.id == KeyEvent.KEY_PRESSED) {
            when (keyEvent.keyCode) {
                KeyEvent.VK_DOWN -> if (viewModel.state.value == State.PLAY) viewModel.moveForce(
                    Coordinates(x = 0, y = 1)
                )

                KeyEvent.VK_LEFT -> if (viewModel.state.value == State.PLAY) viewModel.moveForce(
                    Coordinates(x = -1, y = 0)
                )

                KeyEvent.VK_RIGHT -> if (viewModel.state.value == State.PLAY) viewModel.moveForce(
                    Coordinates(x = 1, y = 0)
                )

                KeyEvent.VK_X -> if (viewModel.state.value == State.PLAY) viewModel.rotatePiece(true)
                KeyEvent.VK_Z -> if (viewModel.state.value == State.PLAY) viewModel.rotatePiece(false)
                KeyEvent.VK_P -> viewModel.switchPausePlay()
                KeyEvent.VK_M -> viewModel.musicState()
                KeyEvent.VK_SPACE -> if (viewModel.state.value == State.PLAY) viewModel.dropPiece()
                KeyEvent.VK_ENTER -> if (viewModel.state.value == State.GAME_OVER) viewModel.restart()
            }
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        val frame = width / TetViewModel.MAX_X

        // Рисование вертикальных линий
        g.color = Color.WHITE
        for (x in 0..TetViewModel.MAX_X) {
            g.drawLine(x * frame, 0, x * frame, height)
        }

        // Рисование горизонтальных линий
        for (y in 0..TetViewModel.MAX_Y) {
            g.drawLine(0, y * frame, width, y * frame)
        }

        // рисование текущей фигуры
        viewModel.pieceCurrent.value.forEach {
            g.drawRect(it.x * frame, it.y * frame, frame, frame) // Рисуем контур
        }

        g.color = getColor()
        viewModel.pieceCurrent.value.forEach {
            g.fillRect(it.x * frame + 1, it.y * frame + 1, frame - 1, frame - 1) // Заполняем с отступом 1 пиксель
        }

        // рисование упавших фигур
        for (y in 0..<TetViewModel.MAX_Y) {
            for (x in 0..<TetViewModel.MAX_X) {
                if (viewModel.fallenPieces[y][x]) {
                    g.color = Color.WHITE // Серый цвет для контура
                    g.drawRect(x * frame, y * frame, frame, frame) // Рисуем контур
                    g.color = getColor()
                    g.fillRect(x * frame + 1, y * frame + 1, frame - 1, frame - 1) // Заполняем с отступом 1 пиксель
                }
            }
        }

        if (viewModel.state.value == State.GAME_OVER) {
            g.color = Color.WHITE
            g.font = Font("Arial", Font.BOLD, 24)
            g.drawString("Game over", frame * 3, frame * 10)
            g.drawString("Press Enter to restart", frame, frame * 12)
        } else if (viewModel.state.value == State.PAUSE) {
            g.color = Color.WHITE
            g.font = Font("Arial", Font.BOLD, 24)
            g.drawString("PAUSE", (frame * 3.5).toInt(), frame * 10)
            g.drawString("Press P to play", frame * 2, frame * 12)
        }
    }

    private fun getColor(): Color {
        return when (viewModel.level.value) {
            0 -> Color(0, 100, 0)
            1 -> Color(0, 60, 50)
            2 -> Color(105, 140, 35)
            3 -> Color(120, 70, 25)
            4 -> Color(45, 30, 10)
            5 -> Color(65, 130, 180)
            6 -> Color(120, 100, 240)
            7 -> Color(0, 0, 140)
            8 -> Color(140, 0, 140)
            else -> Color(75, 0, 130)
        }
    }
}