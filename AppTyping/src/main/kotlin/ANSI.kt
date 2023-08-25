object ANSI {

    const val TEXT_COLOR_RESET = "\u001B[0m"
    const val TEXT_COLOR_RED = "\u001B[31m"
    const val TEXT_COLOR_GREEN = "\u001B[32m"
    const val GREEN_BACKGROUND = "\u001B[42m"

    private const val TEXT_COLOR_GRAY = "\u001B[90m"

    fun moveCursorToColumn(column: Int) {
        print("\u001B[${column}G")
    }

    fun cleanRow(row: Int) {
        print("\u001B[${row};1H\u001B[K")
    }

    fun setCaretPosition(row: Int, column: Int): String {
        return "\u001B[${row};${column}H"
    }

    fun cleanScreen() {
        print("\u001B[H\u001B[2J")
    }

    fun hideCursor() {
        print("\u001B[?25l")
    }

    fun markChar(message: String, index: Int): String {
        return "$TEXT_COLOR_GRAY${message.substring(0, index)}$TEXT_COLOR_RESET" +
                "$TEXT_COLOR_RED${message[index]}$TEXT_COLOR_RESET" +
                message.substring(index + 1)
    }
}