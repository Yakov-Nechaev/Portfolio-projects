import panels.GamePanel
import panels.InfoPanel
import panels.StatusPanel
import java.awt.BorderLayout
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame

class TetApp : JFrame() {

    private val viewModel = TetViewModel()

    init {

        isVisible = true
        title = "Tetris"
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false
        iconImage = ImageIO.read(File("src/res/image/tet_icon.png").inputStream())
        layout = BorderLayout()

        add(GamePanel(viewModel), BorderLayout.WEST)
        add(InfoPanel(viewModel), BorderLayout.EAST)
        add(StatusPanel(viewModel), BorderLayout.SOUTH)

        pack()
        setLocationRelativeTo(null)

        viewModel.restart()

    }
}