package com.tydm.weatherApp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.tydm.weatherApp.R

val montFamily = FontFamily(
    Font(R.font.mont_semibold)
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = montFamily,
        fontSize = 128.sp,
        lineHeight = 128.sp,
        color = WhiteColor
    ),
    headlineMedium = TextStyle(
        fontFamily = montFamily,
        fontSize = 48.sp,
        lineHeight = 64.sp,
        color = WhiteColor
    ),
    headlineSmall = TextStyle(
        fontFamily = montFamily,
        fontSize = 20.sp,
        color = WhiteColor
    ),
    bodyLarge = TextStyle(
        fontFamily = montFamily,
        fontSize = 16.sp,
        color = WhiteColor
    ),
    bodyMedium = TextStyle(
        fontFamily = montFamily,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        color = WhiteColor
    ),
    displayLarge = TextStyle(
        fontFamily = montFamily,
        fontSize = 64.sp,
        color = WhiteColor
    ),
    displayMedium = TextStyle(
        fontFamily = montFamily,
        fontSize = 32.sp,
        lineHeight = 48.sp,
        color = WhiteColor
    )
)