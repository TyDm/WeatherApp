package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import com.tydm.weatherApp.ui.theme.BackgroundDarkColor
import com.tydm.weatherApp.ui.theme.WeatherAppTheme

@Composable
fun TemperatureGradientBackground(
    temperature: Int,
    modifier: Modifier = Modifier
) {
    val windowInfo = LocalWindowInfo.current
    val screenHeight = windowInfo.containerSize.height
    val (centerColor, middleColor, outerColor) = when {
        temperature >= 20 -> {
            Triple(
                Color(0xFFE24030),
                Color(0xFFD2691E),
                Color(0xFFCEB9AA)
            )
        }
        temperature >= 10 -> {
            Triple(
                Color(0xFFD2691E),
                Color(0xFFFF8C42),
                Color(0xFFCEB9AA)
            )
        }
        temperature >= 5 -> {
            Triple(
                Color(0xFFFF8C42),
                Color(0xFFFFA07A),
                Color(0xFFCEB9AA)
            )
        }
        temperature >= -5 -> {
            Triple(
                Color(0xFF2E3E97),
                Color(0xFF5072BB),
                Color(0xFF87CEEB),
//                Color(0xFFCEB9AA)
            )
        }
        temperature >= -10 -> {
            Triple(
                Color(0xFF2B2B74),
                Color(0xFF2E3E97),
                Color(0xFF5072BB),
            )
        }
        temperature >= -20 -> {
            Triple(
                Color(0xFF8B68BF),
                Color(0xFF8A77DC),
                BackgroundDarkColor
            )
        }
        else -> {
            Triple(
                Color(0xFF6007C1),
                Color(0xFF8B68BF),
                BackgroundDarkColor
            )
        }
    }
    val darkRadialGradient = object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val biggerDimension = maxOf(size.height, size.width)
            return RadialGradientShader(
                colors = listOf(
                    Color.Transparent,
                    BackgroundDarkColor),
                center = Offset(size.width/2f, size.height/2.5f),
                radius = biggerDimension / 2.5f,
                colorStops = listOf(0f, 0.9f)
            )
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        centerColor,
                        middleColor,
                        outerColor
                    ),
                    endY = screenHeight.toFloat()/1.3f
                )
            )
            .background(darkRadialGradient)
    )
}

@Preview(backgroundColor = 0xFF222524, showBackground = true, showSystemUi = true)
@Composable
private fun TemperatureGradientBackgroundPreview() {
    WeatherAppTheme {
        TemperatureGradientBackground(
            temperature = 20
        )
    }
}