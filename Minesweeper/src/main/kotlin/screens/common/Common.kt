package screens.common

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun drawRectEdge(drawScope: DrawScope) {
    drawScope.drawRect(
        color = Color.DarkGray,
        topLeft = Offset(0f, 0f),
        size = drawScope.size,
        style = Stroke(width = 1f)
    )
}

fun drawEdgeElevated(drawScope: DrawScope, strokeWidth: Float) {
    drawScope.apply {
        drawLine(
            color = Color.LightGray,
            start = Offset(1f, 1f),
            end = Offset(size.width - 1f, 1f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square
        )

        drawLine(
            color = Color.LightGray,
            start = Offset(1f, 1f),
            end = Offset(1f, size.height - 1f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square
        )

        drawLine(
            color = Color.DarkGray,
            start = Offset(size.width - 1f, 1f),
            end = Offset(size.width - 1f, size.height - 1f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square
        )

        drawLine(
            color = Color.DarkGray,
            start = Offset(1f, size.height - 1f),
            end = Offset(size.width - 1f, size.height - 1f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square
        )
    }
}

fun drawEdgeNotElevated(drawScope: DrawScope, strokeWidth: Float) {

    drawScope.apply {
        drawLine(
            color = Color.DarkGray,
            start = Offset(-2f, -2f),
            end = Offset(size.width + 2f, -2f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square
        )

        drawLine(
            color = Color.DarkGray,
            start = Offset(-2f, -2f),
            end = Offset(-2f, size.height + 2f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square
        )

        drawLine(
            color = Color.LightGray,
            start = Offset(size.width + 2f, -2f),
            end = Offset(size.width + 2f, size.height + 2f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square
        )

        drawLine(
            color = Color.LightGray,
            start = Offset(-2f, size.height + 2f),
            end = Offset(size.width + 2f, size.height + 2f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square
        )
    }
}

