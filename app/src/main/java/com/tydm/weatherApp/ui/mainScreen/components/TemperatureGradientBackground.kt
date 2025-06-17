package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import com.tydm.weatherApp.ui.theme.BackgroundDarkColor
import com.tydm.weatherApp.ui.theme.WeatherAppTheme
import com.tydm.weatherApp.ui.theme.WhiteColor
import kotlin.math.max

@Composable
fun TemperatureGradientBackground(
    temperature: Int,
    modifier: Modifier = Modifier
) {
    val windowInfo = LocalWindowInfo.current
    val screenWidth = windowInfo.containerSize.width
    val screenHeight = windowInfo.containerSize.height
    val (centerColor, middleColor, outerColor) = when {
        temperature >= 35 -> {
            Triple(
                Color(0xFFFF0000),
                Color(0xFF8B0000),
                BackgroundDarkColor
            )
        }
        temperature >= 30 -> {
            Triple(
                Color(0xFFFF4B4B),
                Color(0xFF8B0000),
                BackgroundDarkColor
            )
        }
        temperature >= 25 -> {
            Triple(
                Color(0xFFFF6B3D),
                Color(0xFF8B4513),
                BackgroundDarkColor
            )
        }
        temperature >= 20 -> {
            Triple(
                Color(0xFFFF8C42),
                Color(0xFF8B4513),
                BackgroundDarkColor
            )
        }
        temperature >= 15 -> {
            Triple(
                Color(0xFFFFA07A),
                Color(0xFFD2691E),
                BackgroundDarkColor
            )
        }
        temperature >= 10 -> {
            Triple(
                Color(0xFFFFB74D),
                Color(0xFFE65100),
                BackgroundDarkColor
            )
        }
        temperature >= 5 -> {
            Triple(
                Color(0xFFFFA726),
                Color(0xFFEF6C00),
                BackgroundDarkColor
            )
        }
        temperature >= 0 -> {
            Triple(
                Color(0xFF87CEEB),
                Color(0xFF4682B4),
                BackgroundDarkColor
            )
        }
        temperature >= -5 -> {
            Triple(
                Color(0xFF4B9CD3),
                Color(0xFF1E3A8A),
                BackgroundDarkColor
            )
        }
        temperature >= -10 -> {
            Triple(
                Color(0xFF2B4C7E),
                Color(0xFF1E3A8A), 
                BackgroundDarkColor
            )
        }
        temperature >= -15 -> {
            Triple(
                Color(0xFF1E3A8A),
                Color(0xFF0F172A),
                BackgroundDarkColor
            )
        }
        temperature >= -20 -> {
            Triple(
                Color(0xFF0F172A),
                Color(0xFF1A1B4B),
                BackgroundDarkColor
            )
        }
        else -> {
            Triple(
                Color(0xFF1A1B4B),
                Color(0xFF0A0A2A),
                BackgroundDarkColor
            )
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to centerColor.copy(alpha = 0.85f),
                        0.5f to middleColor.copy(alpha = 0.85f),
                        1f to outerColor
                    ),
                    center = Offset(screenWidth / 2f, screenHeight / 2.4f),
                    radius = max(screenWidth, screenHeight).toFloat()/2.7f
                )
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        WhiteColor.copy(alpha = 0.01f),
                        WhiteColor.copy(alpha = 0.01f),
                        WhiteColor.copy(alpha = 0.05f),
                        WhiteColor.copy(alpha = 0.07f),
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = screenHeight.toFloat()/1.3f
                )
            )
    )
}

@Preview(backgroundColor = 0xFF222524, showBackground = true, showSystemUi = true)
@Composable
private fun TemperatureGradientBackgroundPreview() {
    WeatherAppTheme {
        TemperatureGradientBackground(
            temperature = -5
        )
    }
}