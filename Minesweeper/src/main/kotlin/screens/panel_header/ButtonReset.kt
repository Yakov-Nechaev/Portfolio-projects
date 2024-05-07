package screens.panel_header

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import models.GameState
import screens.common.drawEdgeElevated

@Composable
fun ButtonReset(
    gameState: GameState,
    onResetRequest: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .size(30.dp)
            .clickable(onClick = { onResetRequest() })
            .background(color = Color.Gray)
            .drawBehind { drawEdgeElevated(this, 2.5f) },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(gameState.iconPath),
            contentDescription = gameState.description,
            modifier = Modifier.padding(2.dp),
            tint = Color.Unspecified
        )
    }
}

@Preview
@Composable
fun ResetButtonPreview() {
    Column {
        ButtonReset(GameState.PLAY) {}
        ButtonReset(GameState.WON) {}
        ButtonReset(GameState.LOST) {}
    }
}