package panels

import TetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class PreviewPanel(private val viewModel: TetViewModel) : JPanel() {

    init {
        preferredSize = Dimension(200, 200)
        background = Color(190,225,210)

        // Добавляем слушателя изменений в piece в TetViewModel
        CoroutineScope(Dispatchers.Default).launch {
            viewModel.pieceNext.collect {
                repaint()
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.level.collect {
                repaint()
            }
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        val scale = width / TetViewModel.MAX_X * 2 // Масштабный коэффициент
        val frameWidth = (viewModel.pieceNext.value.maxOf { it.x } + 1) * scale // Ширина фигуры после масштабирования
        val frameHeight = (viewModel.pieceNext.value.maxOf { it.y } + 1) * scale // Высота фигуры после масштабирования
        val offsetX = (width - frameWidth) / 2 // Отступ по горизонтали
        val offsetY = (height - frameHeight) / 2 // Отступ по вертикали

        g.color = when (viewModel.level.value) {
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

        viewModel.pieceNext.value.forEach { coordinates ->
            g.fillRect(offsetX + (coordinates.x - 2) * scale, offsetY + coordinates.y * scale, scale, scale)
        }

        g.color = Color.GRAY
        viewModel.pieceNext.value.forEach { coordinates ->
            g.drawRect(offsetX + (coordinates.x - 2) * scale, offsetY + coordinates.y * scale, scale, scale)
        }
    }

}