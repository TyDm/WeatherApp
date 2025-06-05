package com.tydm.weatherApp.ui.model


import com.tydm.weatherApp.domain.model.*

data class CityWeatherData(
    val city: City,
    val currentWeather: Weather?,
    val dailyForecasts: List<DailyForecast>,
    val hourlyForecasts: List<HourlyForecast>
)

