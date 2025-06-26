package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun ResizableText(
    text: String,
    maxFontSize: TextUnit,
    minFontSize: TextUnit,
    fontStyle: TextStyle = TextStyle(),
    lineHeightMultiplier: Float? = null,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.then(modifier)) {

        val maxWidthPx = this.constraints.maxWidth.toFloat()
        val textMeasurer = rememberTextMeasurer()
        var fontSize by remember { mutableStateOf(maxFontSize) }

        LaunchedEffect(text) {
            var currentSize = maxFontSize.value
            val minSize = minFontSize.value
            while (currentSize >= minSize) {
                val textWidth = textMeasurer.measure(
                    text = text,
                    style = fontStyle.copy(fontSize = currentSize.sp)
                ).size.width

                if (textWidth <= maxWidthPx) {
                    fontSize = currentSize.sp
                    break
                }
                currentSize -= 2
            }
            if (currentSize <= minSize) {
                fontSize = minFontSize
            }
        }
        var textStyle = fontStyle.copy(
            fontSize = fontSize,
            lineHeight = fontSize*(lineHeightMultiplier?:1f)
        )
        Text(text = text, style = textStyle)
    }
}