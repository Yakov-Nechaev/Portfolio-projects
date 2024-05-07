package screens.panel_header

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import models.Digit

@Composable
fun DigitalScreen(count: Int) {
    Row(
        modifier = Modifier.background(Color.DarkGray),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val countString = count.toString().padStart(3, '0')
        countString.forEach {
            val number = it.toString().toInt()
            SegmentDigit(Digit.entries[number])
        }
    }
}

@Composable
fun SegmentDigit(digit: Digit) {
    Canvas(
        Modifier
            .padding(1.dp)
            .requiredSize(15.dp, 30.dp)
    ) {
        val segments = listOf(
            Path().apply {
                //top segment
                moveTo(0f, 0f)
                lineTo(4f, 4f)
                lineTo(size.width - 4f, 4f)
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
            },
            Path().apply {
                // top-left segment
                moveTo(0f, 0f)
                lineTo(4f, 4f)
                lineTo(4f, size.height / 2 - 2f)
                lineTo(0f, size.height / 2)
                lineTo(0f, 0f)
            },
            Path().apply {
                // top-right segment
                moveTo(size.width, 0f)
                lineTo(size.width - 4f, 4f)
                lineTo(size.width - 4f, size.height / 2 - 2f)
                lineTo(size.width, size.height / 2)
                lineTo(size.width, 0f)
            },
            Path().apply {
                // midline segment
                moveTo(0f, size.height / 2)
                lineTo(4f, size.height / 2 - 2f)
                lineTo(size.width - 4f, size.height / 2 - 2f)
                lineTo(size.width, size.height / 2)
                lineTo(size.width - 4f, size.height / 2 + 2f)
                lineTo(4f, size.height / 2 + 2f)
                lineTo(0f, size.height / 2)
            },
            Path().apply {
                // bottom-left segment
                moveTo(0f, size.height / 2)
                lineTo(4f, size.height / 2 + 2f)
                lineTo(4f, size.height - 4f)
                lineTo(0f, size.height)
                lineTo(0f, size.height / 2)
            },
            Path().apply {
                // bottom-right segment
                moveTo(size.width, size.height / 2)
                lineTo(size.width - 4f, size.height / 2 + 2f)
                lineTo(size.width - 4f, size.height - 4f)
                lineTo(size.width, size.height)
                lineTo(size.width, size.height / 2)
            },
            Path().apply {
                // bottom segment
                moveTo(0f, size.height)
                lineTo(4f, size.height - 4f)
                lineTo(size.width - 4f, size.height - 4f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
            })

        segments.forEachIndexed { index, path ->
            drawPath(
                path = path, color = Color.Red,
                alpha = if (digit.pattern[index]) 1f else 0.1f,
                style = Fill
            )
        }

        segments.forEachIndexed { index, path ->
            drawPath(
                path = path, color = Color.Black,
                alpha = if (digit.pattern[index]) 1f else 0f,
                style = Stroke(width = 1f)
            )
        }
    }
}

@Preview
@Composable
private fun SegmentDigitalPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (segment in Digit.entries) {
            SegmentDigit(segment)
        }
    }
}

@Preview
@Composable
fun DigitalScreenPreview() {
    Column {
        DigitalScreen(956)
        DigitalScreen(88)
        DigitalScreen(56)
        DigitalScreen(4)
        DigitalScreen(0)
        DigitalScreen(777)
    }
}

@Composable
fun ColonSeparator() {
    Canvas(Modifier.requiredSize(10.dp, 30.dp).background(Color.DarkGray)) {
        drawCircle(Color.Red, 2.5f, Offset(size.width / 2, size.height / 3))
        drawCircle(Color.Red, 2.5f, Offset(size.width / 2, size.height - size.height / 3))
    }
}

@Preview
@Composable
fun ColonSeparatorPreview() {
    ColonSeparator()
}