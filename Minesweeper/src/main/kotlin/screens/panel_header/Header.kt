package screens.panel_header

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import models.GameState
import screens.common.drawEdgeNotElevated

@Composable
fun PanelHeaderLayout(
    flagsRemaining: Int,
    seconds: Int,
    gameState: GameState,
    onResetRequest: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Gray)
            .padding(10.dp)
            .drawBehind { drawEdgeNotElevated(this, 3f) },
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DigitalScreen(count = flagsRemaining)
        ButtonReset(gameState = gameState, onResetRequest = onResetRequest)
        DigitalScreen(count = seconds)
    }
}

@Preview
@Composable
fun PanelHeaderPreview() {
    Box(
        modifier = Modifier.width(240.dp)
    ) {
        PanelHeaderLayout(flagsRemaining = 10, seconds = 30, gameState = GameState.PLAY) {}
    }
}