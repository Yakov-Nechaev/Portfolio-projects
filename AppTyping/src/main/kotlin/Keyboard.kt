import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Keyboard {

    private val keyBoard = listOf(
        listOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '<'),
        listOf('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']'),
        listOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\''),
        listOf('z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/'),
        listOf(' ')
    )

    private val keyBoardUpperCase = listOf(
        listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '<'),
        listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', '[', ']'),
        listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', ':', '"'),
        listOf('Z', 'X', 'C', 'V', 'B', 'N', 'M', '<', '>', '?'),
        listOf(' ')
    )

    var timer = 50
    var counter = 0
    var message = ""
    var length = 0
    var currentChar = '`'
    private val keyBoardMutex = Mutex()
    val maxLengthMessage = 75

    suspend fun draw() {
        keyBoardMutex.withLock {

            val currentBoard = if (currentChar.isUpperCase()) keyBoardUpperCase else keyBoard

            drawMonitor()
            drawNumberRow(currentBoard[0])
            drawLetterRow(currentBoard[1], 15, 24)
            drawLetterRow(currentBoard[2], 18, 27)
            drawLetterRow(currentBoard[3], 21, 30)
            drawSpaceRow(currentBoard[4], currentChar.isUpperCase())

        }
    }

    private fun drawMonitor() {

        println(ANSI.setCaretPosition(2, 21) + " " + "_".repeat(77) + " ")
        print(ANSI.setCaretPosition(3, 21) + "| COUNTER: ${ANSI.TEXT_COLOR_GREEN}$counter${ANSI.TEXT_COLOR_RESET}")

        when (timer) {
            in 0..9 -> print(
                ANSI.setCaretPosition(3, 89) + "TIMER: ${ANSI.TEXT_COLOR_RED}$timer${ANSI.TEXT_COLOR_RESET} "
            )

            in 10..99 -> print(
                ANSI.setCaretPosition(3, 89) + "TIMER: ${ANSI.TEXT_COLOR_RED}$timer${ANSI.TEXT_COLOR_RESET}"
            )
        }

        println(ANSI.setCaretPosition(3, 99) + "|")

        println(ANSI.setCaretPosition(4, 21) + "|" + " ".repeat(77) + "|")
        println(ANSI.setCaretPosition(5, 21) + "|" + " ".repeat(77) + "|")
        println(ANSI.setCaretPosition(6, 21) + "|" + " ".repeat(77) + "|")

        ANSI.cleanRow(7)
        print(ANSI.setCaretPosition(7, 21) + "|")
        val space = (80 - length) / 2 + 20
        print(ANSI.setCaretPosition(7, space) + message)
        println(ANSI.setCaretPosition(7, 99) + "|")

        println(ANSI.setCaretPosition(8, 21) + "|" + " ".repeat(77) + "|")
        println(ANSI.setCaretPosition(9, 21) + "|" + " ".repeat(77) + "|")
        println(ANSI.setCaretPosition(10, 21) + "|" + " ".repeat(77) + "|")
    }

    private fun drawNumberRow(list: List<Char>) {
        println(ANSI.setCaretPosition(11, 21) + "|" + "_____ ".repeat(list.size - 1) + "_____|")

        ANSI.moveCursorToColumn(21)
        for (i in list) {
            if (i == currentChar) print("|${ANSI.GREEN_BACKGROUND}     ${ANSI.TEXT_COLOR_RESET}")
            else print("|     ")
        }
        println("|")

        ANSI.moveCursorToColumn(21)
        for (i in list) {
            if (i == currentChar) print("|${ANSI.GREEN_BACKGROUND}  $i  ${ANSI.TEXT_COLOR_RESET}")
            else print("|  $i  ")
        }
        println("|")

        ANSI.moveCursorToColumn(21)
        for (i in list) {
            if (i == currentChar) print("|${ANSI.GREEN_BACKGROUND}_____${ANSI.TEXT_COLOR_RESET}")
            else print("|_____")
        }
        print("|")
    }

    private fun drawLetterRow(list: List<Char>, row: Int, column: Int) {

        print(ANSI.setCaretPosition(row, column))
        for (i in list) {
            if (i == currentChar) print("|${ANSI.GREEN_BACKGROUND}     ${ANSI.TEXT_COLOR_RESET}")
            else print("|     ")
        }
        println("|")

        ANSI.moveCursorToColumn(column)
        for (i in list) {
            if (i == currentChar) print("|${ANSI.GREEN_BACKGROUND}  $i  ${ANSI.TEXT_COLOR_RESET}")
            else print("|  $i  ")

        }
        println("|")

        ANSI.moveCursorToColumn(column)
        for (i in list) {
            if (i == currentChar) print("|${ANSI.GREEN_BACKGROUND}_____${ANSI.TEXT_COLOR_RESET}")
            else print("|_____")
        }
        println("|")
    }

    private fun drawSpaceRow(list: List<Char>, caps: Boolean) {

        print(ANSI.setCaretPosition(24, 30))
        if (caps) print("|${ANSI.GREEN_BACKGROUND}           ${ANSI.TEXT_COLOR_RESET}")
        else print("|           ")

        for (i in list) {
            if (i == currentChar) print("|" + ANSI.GREEN_BACKGROUND + " ".repeat(35) + ANSI.TEXT_COLOR_RESET)
            else print("|" + " ".repeat(35))
        }
        println("|           |")

        ANSI.moveCursorToColumn(30)
        if (caps) print("|${ANSI.GREEN_BACKGROUND} S H I F T ${ANSI.TEXT_COLOR_RESET}")
        else print("| S H I F T ")

        for (i in list) {
            if (i == currentChar) print("|" + ANSI.GREEN_BACKGROUND + " ".repeat(35) + ANSI.TEXT_COLOR_RESET)
            else print("|" + " ".repeat(35))
        }
        println("|    ENG    |")

        ANSI.moveCursorToColumn(30)
        if (caps) print("|${ANSI.GREEN_BACKGROUND}___________${ANSI.TEXT_COLOR_RESET}")
        else print("|___________")

        for (i in list) {
            if (i == currentChar) print("|" + ANSI.GREEN_BACKGROUND + "_".repeat(35) + ANSI.TEXT_COLOR_RESET)
            else print("|" + "_".repeat(35))
        }
        println("|___________|")
    }
}