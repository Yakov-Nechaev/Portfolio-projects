import kotlinx.coroutines.channels.Channel
import java.awt.Dimension
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JFrame
import javax.swing.JTextArea

class Frame : JFrame() {

    val charChannel = Channel<Char>()

    init {
        title = "IMPORTANT!!! This window must be active to catch key presses"
        size = Dimension(550, 70)
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE

        val descriptionTextArea = JTextArea().apply {
            text = "  You can exit the application by pressing the ESC or close that window"
            isEditable = false
            lineWrap = true
            wrapStyleWord = true
        }
        add(descriptionTextArea)
        descriptionTextArea.revalidate()

        addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent) {}

            override fun keyPressed(e: KeyEvent) {
                    charChannel.trySend(e.keyChar)
            }

            override fun keyReleased(e: KeyEvent) {}
        })
    }

}