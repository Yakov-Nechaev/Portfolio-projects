package com.example.mysn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SnakeScreenLogic() {

    val snakeViewModel: SnakeViewModel = viewModel()
    LaunchedEffect(Unit) {
        snakeViewModel.restart()
    }

    val food = snakeViewModel.food.collectAsState()
    val snake = snakeViewModel.snake.collectAsState()
    val length = snakeViewModel.length.collectAsState()
    val classicMode = snakeViewModel.classicMode.collectAsState()
    val state = snakeViewModel.play.collectAsState()
    val direction = snakeViewModel.direction.collectAsState()

    SnakeScreenLayout(
        food = food.value,
        snake = snake.value,
        classicMode = classicMode.value,
        amount = length.value,
        state = state.value,
        direction = direction.value,
        onChangeDirection = { snakeViewModel.changeDirection(it) },
        onChangeMode = { snakeViewModel.changeMode() },
        onChangePlay = { snakeViewModel.changePlay() }
    )
}

@Composable
fun SnakeScreenLayout(
    food: Coordinates,
    snake: List<Coordinates>,
    classicMode: Boolean,
    amount: Int,
    state: Boolean,
    direction: Coordinates,
    onChangeDirection: (intent: Coordinates) -> Unit,
    onChangeMode: () -> Unit,
    onChangePlay: () -> Unit
) {
    // All screen
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Game field
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onChangePlay() }
        ) {
            // Score panel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = 24.dp, end = 16.dp)
                    .toggleable(value = classicMode, onValueChange = { onChangeMode() }),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(text = "${amount - 4}", fontSize = 24.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "classic", modifier = Modifier.padding(end = 8.dp))
                Checkbox(checked = classicMode, onCheckedChange = null)
            }

            // Game panel
            BoxWithConstraints(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {

                val frame = maxWidth / SnakeViewModel.STEP

                Box(
                    modifier = Modifier
                        .size(maxWidth)
                        .border(2.dp, Color.Blue)
                )

                Box(
                    modifier = Modifier
                        .offset(x = frame * food.x, y = frame * food.y)
                        .size(frame)
                        .background(color = Color.Green, shape = CircleShape)

                )

                snake.forEach {
                    Box(
                        modifier = Modifier
                            .offset(x = frame * it.x, y = frame * it.y)
                            .size(frame)
                            .background(color = Color.Magenta, shape = Shapes().extraSmall)
                    )
                }
            }

            // State panel
            Column(modifier = Modifier.height(56.dp)) {
                AnimatedVisibility(
                    visible = !state,
                    enter = fadeIn(animationSpec = tween(1000)),
                    exit = fadeOut(animationSpec = tween(2000))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (state) "PLAY" else "PAUSE (${direction.mapToString()})"
                        )
                    }
                }
            }

        }

        // Buttons field
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 24.dp, end = 24.dp)
        ) {
            Button(
                onClick = { onChangeDirection(Coordinates(x = 0, y = -1)) },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "up"
                )
            }

            Row {
                Button(
                    onClick = { onChangeDirection(Coordinates(x = -1, y = 0)) },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "left")
                }

                Spacer(modifier = Modifier.size(80.dp))

                Button(
                    onClick = { onChangeDirection(Coordinates(x = 1, y = 0)) },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "right"
                    )
                }
            }

            Button(
                onClick = { onChangeDirection(Coordinates(x = 0, y = 1)) },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "down"
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun Preview() {
    SnakeScreenLogic()
}