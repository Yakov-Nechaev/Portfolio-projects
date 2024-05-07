package screens.panel_game

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import models.Cell
import models.CellState
import models.Coordinates
import models.GameState
import screens.common.drawEdgeElevated
import screens.common.drawRectEdge

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CellElement(
    gameState: GameState,
    cell: Cell,
    onSelectedCell: (Coordinates) -> Unit,
    onFlagCell: (Coordinates) -> Unit
) {
    Box(
        modifier = Modifier
            .requiredSize(20.dp)
            .background(
                if (cell.isMine && gameState == GameState.LOST && cell.state == CellState.SELECTED) Color.Red else Color.Gray
            )
            .drawBehind {
                if (cell.state == CellState.SELECTED ||
                    gameState == GameState.LOST && cell.isMine && cell.state == CellState.UNSELECTED
                ) {
                    drawRectEdge(drawScope = this)
                } else {
                    drawEdgeElevated(drawScope = this, strokeWidth = 2.2f)
                }
            }

            .onClick(
                matcher = PointerMatcher.mouse(PointerButton.Primary),
                onClick = { onSelectedCell(cell.coordinates) },
            )
            .onClick(
                matcher = PointerMatcher.mouse(PointerButton.Secondary),
                onClick = { onFlagCell(cell.coordinates) }
            ),

        contentAlignment = Alignment.Center

    ) {

        when (cell.state) {

            CellState.UNSELECTED -> {

                if (gameState == GameState.LOST && cell.isMine) {
                    Icon(
                        painter = painterResource("mine.svg"),
                        contentDescription = "mine",
                        modifier = Modifier.padding(3.dp),
                        tint = Color.Unspecified
                    )
                }

                if (gameState == GameState.WON && cell.isMine) {
                    Icon(
                        painter = painterResource("flag.svg"),
                        contentDescription = "flag",
                        modifier = Modifier.padding(3.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            CellState.SELECTED -> {
                if (cell.isMine) {
                    Icon(
                        painter = painterResource("mine.svg"),
                        contentDescription = "mine",
                        modifier = Modifier.padding(3.dp),
                        tint = Color.Unspecified
                    )
                } else if (cell.neighbourMines != 0) {
                    Text(
                        text = cell.neighbourMines.toString(),
                        color = listOf(
                            Color.Blue, Color.Green, Color.Cyan, Color.Yellow,
                            Color.Red, Color.Magenta, Color.DarkGray, Color.Black
                        )[cell.neighbourMines - 1],
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,
                    )
                }
            }

            CellState.FLAGGED -> {
                if (gameState == GameState.LOST && !cell.isMine) {
                    Icon(
                        painter = painterResource("mine_x.svg"),
                        contentDescription = "mine_x",
                        modifier = Modifier.padding(3.dp),
                        tint = Color.Unspecified
                    )
                } else {
                    Icon(
                        painter = painterResource("flag.svg"),
                        contentDescription = "flag",
                        modifier = Modifier.padding(3.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CellPreview() {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Standard representation of an unselected cell without a mine
        CellElement(GameState.PLAY, Cell(Coordinates(0, 0)), {}, {})
        CellElement(GameState.WON, Cell(Coordinates(0, 0)), {}, {})
        CellElement(GameState.LOST, Cell(Coordinates(0, 0)), {}, {})

        // Standard representation of a selected cell without a mine
        CellElement(GameState.PLAY, Cell(Coordinates(0, 0), state = CellState.SELECTED), {}, {})
        CellElement(GameState.WON, Cell(Coordinates(0, 0), state = CellState.SELECTED), {}, {})
        CellElement(GameState.LOST, Cell(Coordinates(0, 0), state = CellState.SELECTED), {}, {})

        // A cell without a mine incorrectly marked with a flag during the game
        CellElement(GameState.PLAY, Cell(Coordinates(0, 0), state = CellState.FLAGGED), {}, {})

        // A cell without a mine incorrectly marked with a flag after losing the game
        CellElement(GameState.LOST, Cell(Coordinates(0, 0), state = CellState.FLAGGED), {}, {})
    }
}

@Preview
@Composable
fun CellWithMinePreview() {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // A mined cell unselected during the game
        CellElement(GameState.PLAY, Cell(Coordinates(0, 0), CellState.UNSELECTED, true), {}, {})

        // A mined cell flagged during the game
        CellElement(GameState.PLAY, Cell(Coordinates(0, 0), CellState.FLAGGED, true), {}, {})

        // A mined cell selected, resulting in a game over
        CellElement(GameState.LOST, Cell(Coordinates(0, 0), CellState.SELECTED, true), {}, {})

        // A mined cell unselected when the game is lost
        CellElement(GameState.LOST, Cell(Coordinates(0, 0), CellState.UNSELECTED, true), {}, {})

        // A mined cell flagged when the game is lost
        CellElement(GameState.LOST, Cell(Coordinates(0, 0), CellState.FLAGGED, true), {}, {})

        // A mined cell unselected when the game is won
        CellElement(GameState.WON, Cell(Coordinates(0, 0), CellState.UNSELECTED, true), {}, {})

        // A mined cell flagged when the game is won
        CellElement(GameState.WON, Cell(Coordinates(0, 0), CellState.FLAGGED, true), {}, {})
    }
}

@Preview
@Composable
private fun CellWithTextPreview() {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        (1..8).forEach {
            CellElement(GameState.PLAY, Cell(Coordinates(0, 0), CellState.SELECTED, neighbourMines = it), {}, {})
        }
    }
}