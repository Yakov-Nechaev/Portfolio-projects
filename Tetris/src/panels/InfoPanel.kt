package panels

import TetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class InfoPanel(private val viewModel: TetViewModel) : JPanel() {

    private val scoreLabel = JLabel("score: 0", SwingConstants.CENTER)
    private val levelLabel = JLabel("level: 1", SwingConstants.CENTER)
    private val previewPanel = PreviewPanel(viewModel)

    init {

        scoreLabel.preferredSize = Dimension(160, 200)
        levelLabel.preferredSize = Dimension(160, 200)
        previewPanel.preferredSize = Dimension(160, 200)


        scoreLabel.font = Font("Arial", Font.BOLD, 18)
        levelLabel.font = Font("Arial", Font.PLAIN, 18)

        layout = BorderLayout()

        add(scoreLabel, BorderLayout.CENTER)
        add(previewPanel, BorderLayout.NORTH)
        add(levelLabel, BorderLayout.SOUTH)

        CoroutineScope(Dispatchers.Default).launch {

            launch {
                viewModel.score.collect {
                    scoreLabel.text = "score: $it"
                }
            }

            launch {
                viewModel.level.collect {
                    levelLabel.text = "level: ${it + 1}"
                }
            }

        }

    }
}