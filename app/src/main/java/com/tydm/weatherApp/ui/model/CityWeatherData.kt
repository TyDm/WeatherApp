package com.tydm.weatherApp.ui.model


import com.tydm.weatherApp.domain.model.*

data class CityWeatherData(
    val city: City,
    val currentWeather: Weather? = null,
    val dailyForecasts: List<DailyForecast> = emptyList(),
    val hourlyForecasts: List<HourlyForecast> = emptyList(),
    val isLoading: Boolean = false
)

