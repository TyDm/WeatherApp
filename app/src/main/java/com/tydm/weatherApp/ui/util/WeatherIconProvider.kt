package com.tydm.weatherApp.ui.util

import com.tydm.weatherApp.R

object WeatherIconProvider {
    fun getIconRes(conditionCode: Int): Int {
        return when (conditionCode) {
            1,2,3 -> R.drawable.ic_weather_sunny
            4,5,6,20,21 -> R.drawable.ic_weather_partly_sunny
            7,8,11,19 -> R.drawable.ic_weather_cloudy
            12,39,40 -> R.drawable.ic_weather_showers
            13,14 -> R.drawable.ic_weather_partly_sunny_showers
            15 -> R.drawable.ic_weather_storms
            16, 17,41,42 -> R.drawable.ic_weather_partly_sunny_storms
            18 -> R.drawable.ic_weather_rain
            22,24,25 -> R.drawable.ic_weather_snow
            23 -> R.drawable.ic_weather_mostly_cloudy_snow
            26,29 -> R.drawable.ic_weather_freezing_rain
            30 -> R.drawable.ic_weather_hot
            33,34 -> R.drawable.ic_weather_clear
            else -> R.drawable.ic_weather_cloudy
        }
    }
}