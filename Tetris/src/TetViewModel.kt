import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import models.Coordinates
import models.SoundMode
import models.State
import models.Tracks

class TetViewModel {

    private val mutex = Mutex()

    val soundMode = MutableStateFlow(SoundMode.SoundAndMusic)
    val soundTrack = MutableSharedFlow<Tracks>(replay = 0)

    val state = MutableStateFlow(State.PLAY)
    val pieceCurrent = MutableStateFlow(listOf<Coordinates>())
    val pieceNext = MutableStateFlow(listOf<Coordinates>())
    val score = MutableStateFlow(0)
    var level = MutableStateFlow(0)
    private val speed = MutableStateFlow(1000)
    var fallenPieces = MutableList(MAX_Y) { MutableList(MAX_X) { false } }

    fun musicState() {
        soundMode.value = when (soundMode.value) {
            SoundMode.SoundAndMusic -> SoundMode.Sound
            SoundMode.Sound -> SoundMode.Mute
            SoundMode.Mute -> SoundMode.SoundAndMusic
        }
    }

    fun restart() {
        fallenPieces = MutableList(MAX_Y) { MutableList(MAX_X) { false } }
        score.value = 0
        level.value = 0
        speed.value = 1000
        createNewPiece()
        state.value = State.PLAY
        move()
    }

    fun switchPausePlay() {
        if (state.value == State.PLAY) {
            state.value = State.PAUSE
        } else if (state.value == State.PAUSE) {
            state.value = State.PLAY
            move()
        }
    }

    private fun move() {
        CoroutineScope(Dispatchers.Default).launch {

            while (state.value == State.PLAY) {
                delay(speed.value.toLong())

                val newPiece = pieceCurrent.value.map { Coordinates(it.x, it.y + 1) }

                if (isMoveAllowed(newPiece)) {
                    mutex.withLock { pieceCurrent.value = newPiece }
                } else if (pieceCurrent.value.any { it.y <= 0 }) {
                    state.value = State.GAME_OVER
                } else {
                    for (coordinates in pieceCurrent.value) {
                        fallenPieces[coordinates.y][coordinates.x] = true
                    }

                    removeFullRows()
                    createNewPiece()
                }
            }
        }
    }

    fun moveForce(coordinates: Coordinates) {
        CoroutineScope(Dispatchers.Default).launch {
            val newPiece = pieceCurrent.value.map { Coordinates(it.x + coordinates.x, it.y + coordinates.y) }
            if (isMoveAllowed(newPiece)) {
                mutex.withLock { pieceCurrent.value = newPiece }
                soundTrack.emit(Tracks.Move)
            }
        }
    }

    fun dropPiece() {
        CoroutineScope(Dispatchers.Default).launch {
            var newPiece = pieceCurrent.value.map { Coordinates(it.x, it.y + 1) }
            while (isMoveAllowed(newPiece)) {
                mutex.withLock { pieceCurrent.value = newPiece }
                newPiece = newPiece.map { Coordinates(it.x, it.y + 1) }
            }
            soundTrack.emit(Tracks.Drop)
        }
    }

    private fun isMoveAllowed(newPiece: List<Coordinates>): Boolean {
        return newPiece.all { it.x in 0..<MAX_X && it.y >= 0 && it.y < MAX_Y }
                &&
                newPiece.none { fallenPieces[it.y][it.x] }
    }

    private fun removeFullRows() {
        CoroutineScope(Dispatchers.Default).launch {
            val fullRows = mutableListOf<Int>()
            for (y in 0..<MAX_Y) {
                if (fallenPieces[y].all { it }) {
                    fullRows.add(y)
                }
            }

            when (fullRows.size) {
                1 -> {
                    soundTrack.emit(Tracks.OneLine)
                    score.value += 500
                }

                2 -> {
                    soundTrack.emit(Tracks.TwoLines)
                    score.value += 1500
                }

                3 -> {
                    soundTrack.emit(Tracks.ThreeLines)
                    score.value += 3500
                }

                4 -> {
                    soundTrack.emit(Tracks.FourLines)
                    score.value += 6000
                }
            }

            for (rowIndex in fullRows) {
                fallenPieces.removeAt(rowIndex)
                fallenPieces.add(0, MutableList(MAX_X) { false })
            }

            if (level.value < 9) {
                level.value = (score.value / 10000)
                speed.value = 1000 - (level.value * 100)
            }
        }
    }

    fun rotatePiece(clockwise: Boolean) {
        CoroutineScope(Dispatchers.Default).launch {

            val center = findCenterOfPiece(pieceCurrent.value)

            // Вычисляем новые координаты фигуры после поворота
            var newPiece = pieceCurrent.value.map { coordinates ->
                // Определяем переменные для новых координат
                val nX: Int // Новая координата x
                val nY: Int // Новая координата y

                // Проверяем, осуществляется ли поворот по часовой стрелке
                if (clockwise) {
                    // Если поворот по часовой стрелке, вычисляем новые координаты точки используя следующие формулы:
                    // nx = cx + (cy - y)
                    // ny = cy - (cx - x)
                    // Где cx и cy - координаты центра поворота, x и y - текущие координаты точки
                    nX = center.x + (center.y - coordinates.y)
                    nY = center.y - (center.x - coordinates.x)
                } else {
                    // Если поворот против часовой стрелки, используем аналогичные формулы, но с измененными знаками:
                    // dx = cx - (cy - y)
                    // dy = cy + (cx - x)
                    nX = center.x - (center.y - coordinates.y)
                    nY = center.y + (center.x - coordinates.x)
                }
                // Возвращаем новые координаты в виде объекта Coordinates
                Coordinates(nX, nY)
            }
            // Находим новый центр фигуры после поворота
            val centerNew = findCenterOfPiece(newPiece)

            // Вычисляем сдвиг по X и Y
            val diffX = center.x - centerNew.x
            val diffY = center.y - centerNew.y

            // Применяем сдвиг ко всем координатам фигуры
            newPiece = newPiece.map { Coordinates(it.x + diffX, it.y + diffY) }

            // Сдвигаем фигуру, если она выходит за пределы поля
            if (!isMoveAllowed(newPiece)) {
                val shift = (-2..2).firstOrNull { x ->
                    isMoveAllowed(newPiece.map { Coordinates(it.x + x, it.y) })
                } ?: 0
                newPiece = newPiece.map { Coordinates(it.x + shift, it.y) }
            }

            // Проверяем снова, помещается ли фигура ведь она может быть зажата между другими деталями
            if (isMoveAllowed(newPiece)) {
                soundTrack.emit(Tracks.Rotation)
                mutex.withLock { pieceCurrent.value = newPiece }
            }
        }
    }

    private fun findCenterOfPiece(piece: List<Coordinates>): Coordinates {
        val minX = piece.minOf { it.x }
        val minY = piece.minOf { it.y }
        val maxX = piece.maxOf { it.x }
        val maxY = piece.maxOf { it.y }
        val centerX = (minX + maxX) / 2
        val centerY = (minY + maxY) / 2
        return Coordinates(centerX, centerY)
    }

    private fun createNewPiece() {
        val piecesList = listOf(
            listOf(Coordinates(0, 0), Coordinates(1, 0), Coordinates(0, 1), Coordinates(1, 1)), // Квадрат
            listOf(Coordinates(0, 0), Coordinates(0, 1), Coordinates(0, 2), Coordinates(0, 3)), // Линия
            listOf(Coordinates(0, 0), Coordinates(0, 1), Coordinates(0, 2), Coordinates(1, 0)), // L-фигура
            listOf(Coordinates(0, 0), Coordinates(1, 0), Coordinates(1, 1), Coordinates(1, 2)), // Зеркальная L-фигура
            listOf(Coordinates(0, 0), Coordinates(0, 1), Coordinates(1, 1), Coordinates(0, 2)), // T-фигура
            listOf(Coordinates(0, 0), Coordinates(0, 1), Coordinates(1, 1), Coordinates(1, 2)), // Зеркальная S-фигура
            listOf(Coordinates(1, 0), Coordinates(1, 1), Coordinates(0, 1), Coordinates(0, 2)), // S-фигура
        )

        val newPiece = piecesList.random().map { Coordinates(it.x + 4, it.y) }

        // проверяем если это первый запуск игры, создаем следующую фигуру и вызываем функцию еще раз
        if (pieceNext.value.isEmpty()) {
            pieceNext.value = newPiece
            createNewPiece()
        } else {
            pieceCurrent.value = pieceNext.value
            pieceNext.value = newPiece
        }
    }

    // Размеры игрового поля
    companion object {
        const val MAX_X = 10
        const val MAX_Y = 20
    }
}