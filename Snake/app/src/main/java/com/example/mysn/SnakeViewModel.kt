package com.example.mysn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class SnakeViewModel : ViewModel() {

    val food = MutableStateFlow(Coordinates(x = 0, y = 0))
    val snake = MutableStateFlow(
        listOf(
            Coordinates(x = 10, y = 7),
            Coordinates(x = 9, y = 7),
            Coordinates(x = 8, y = 7),
            Coordinates(x = 7, y = 7)
        )
    )
    val length = MutableStateFlow(4)
    val classicMode = MutableStateFlow(true)
    val direction = MutableStateFlow(Coordinates(x = 1, y = 0))
    val play = MutableStateFlow(false)

    fun changeMode() {
        classicMode.value = !classicMode.value
    }

    fun changePlay() {
        if (play.value) {
            play.value = false
        } else {
            play.value = true
            moveSnake()
        }
    }

    private fun newFood() {
        var newFoodCoordinates: Coordinates

        do {
            newFoodCoordinates = Coordinates(x = Random.nextInt(STEP), y = Random.nextInt(STEP))
        } while (snake.value.contains(newFoodCoordinates) || newFoodCoordinates == food.value)

        food.value = newFoodCoordinates
    }

    fun restart() {
        play.value = false
        snake.value = listOf(
            Coordinates(x = 10, y = 7),
            Coordinates(x = 9, y = 7),
            Coordinates(x = 8, y = 7),
            Coordinates(x = 7, y = 7)
        )
        length.value = 4
        direction.value = Coordinates(x = 1, y = 0)
        newFood()
    }

    fun changeDirection(newDirection: Coordinates) {
        if (!play.value) {
            play.value = true
            moveSnake()
        }

        if (newDirection.x != -direction.value.x || newDirection.y != -direction.value.y) {
            direction.value = newDirection
        }
    }

    private fun moveSnake() {
        viewModelScope.launch {
            while (play.value) {
                delay(200)
                val newPosition = snake.value.first().let {
                    if (classicMode.value) {
                        val newX = it.x + direction.value.x
                        val newY = it.y + direction.value.y
                        Coordinates(x = newX, y = newY)
                    } else {
                        val newX = (it.x + direction.value.x + STEP) % STEP
                        val newY = (it.y + direction.value.y + STEP) % STEP
                        Coordinates(x = newX, y = newY)
                    }
                }

                if (newPosition == food.value) {
                    length.value++
                    snake.value = listOf(newPosition) + snake.value.take(length.value - 1)
                    newFood()
                } else if (snake.value.contains(newPosition) ||
                    newPosition.x == STEP || newPosition.x < 0 ||
                    newPosition.y == STEP || newPosition.y < 0
                ) {
                    restart()
                } else {
                    snake.value = listOf(newPosition) + snake.value.take(length.value - 1)
                }
            }
        }
    }

    companion object {
        const val STEP = 20
    }
}