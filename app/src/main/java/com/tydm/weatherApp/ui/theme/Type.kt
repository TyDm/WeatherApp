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
    headlineMedium = TextStyle(
        fontFamily = montFamily,
        fontSize = 48.sp,
        color = WhiteText
    ),
    headlineLarge = TextStyle(
        fontFamily = montFamily,
        fontSize = 128.sp,
        lineHeight = 128.sp,
        color = WhiteText
    ),
    bodySmall = TextStyle(
        fontFamily = montFamily,
        fontSize = 16.sp,
        color = WhiteText
    ),
    bodyMedium = TextStyle(
        fontFamily = montFamily,
        fontSize = 20.sp,
        color = WhiteText
    )
)