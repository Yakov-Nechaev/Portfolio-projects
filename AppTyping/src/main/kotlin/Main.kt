import kotlinx.coroutines.*
import java.awt.event.KeyEvent
import kotlin.system.exitProcess

suspend fun main(args: Array<String>) {

    val keyBoard = Keyboard()
    val frame = Frame()
    var modeInternet = true
    var modeTimer = true

    if (args.contains("no_internet")) modeInternet = false
    if (args.contains("no_timer")) modeTimer = false

    welcomeScreen()
    frame.charChannel.receive()
    ANSI.cleanScreen()

    coroutineScope {

        launch(Dispatchers.IO) {
            while (true) {

                val responseString =
                    if (modeInternet) Network.apiService.getRandomActivity().activity else Sentences.list.random()

                if (responseString.length < keyBoard.maxLengthMessage) {

                    keyBoard.apply {
                        timer = responseString.length
                        message = responseString
                        length = responseString.length
                    }

                    for (index in responseString.indices) {
                        keyBoard.apply {
                            message = ANSI.markChar(message = responseString, index = index)
                            currentChar = responseString[index]
                            draw()
                        }

                        var char = frame.charChannel.receive()

                        while (char != responseString[index]) {
                            if (char == KeyEvent.VK_ESCAPE.toChar()) {
                                keyBoard.timer = 0
                            }
                            char = frame.charChannel.receive()
                        }
                    }
                    keyBoard.counter++
                }
            }
        }

        launch {
            while (keyBoard.timer > 0) {
                delay(1000)
                if (modeTimer) keyBoard.timer--
                keyBoard.draw()
            }
        }.join()

        coroutineContext.cancelChildren()
    }

    keyBoard.apply {
        currentChar = '`'
        length = 40
        message = "COMPLETED TASKS IS ${keyBoard.counter}. THANKS FOR THE GAME!"
        draw()
    }

    frame.dispose()
    exitProcess(0)
}

fun welcomeScreen() {
    ANSI.hideCursor()
    ANSI.cleanScreen()

    print(ANSI.setCaretPosition(2, 21) + " " + "_".repeat(77) + " ")
    print(ANSI.setCaretPosition(3, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(4, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(5, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(6, 21) + "|" + " ".repeat(13) +
            "Welcome to the keyboard typing practice application" + " ".repeat(13) + "|")
    print(ANSI.setCaretPosition(7, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(8, 21) + "|" + " ".repeat(15) +
            "Please check:" + " ".repeat(49) + "|")
    print(ANSI.setCaretPosition(9, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(10, 21) + "|" + " ".repeat(17) +
            "* English keyboard layout is enabled" + " ".repeat(24) + "|")
    print(ANSI.setCaretPosition(11, 21) + "|" + " ".repeat(17) +
            "* Internet is connected" + " ".repeat(37) + "|")
    print(ANSI.setCaretPosition(12, 21) + "|" + " ".repeat(17) +
            "  (app takes phrases from the internet)" + " ".repeat(21) + "|")
    print(ANSI.setCaretPosition(13, 21) + "|" + " ".repeat(17) +
            "* The \"IMPORTANT\" window is active" + " ".repeat(26) + "|")
    print(ANSI.setCaretPosition(14, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(15, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(16, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(17, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(18, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(19, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(20, 21) + "|" + " ".repeat(27) +
            "PRESS ANY KEY TO START" + " ".repeat(28) + "|")
    print(ANSI.setCaretPosition(21, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(22, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(23, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(24, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(25, 21) + "|" + " ".repeat(77) + "|")
    print(ANSI.setCaretPosition(26, 21) + "|" + "_".repeat(77) + "|")

}