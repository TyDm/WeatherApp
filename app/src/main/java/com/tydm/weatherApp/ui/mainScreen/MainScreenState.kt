package com.tydm.weatherApp.ui.mainScreen
//

import com.tydm.weatherApp.ui.model.CityWeatherData

data class MainScreenState(
    val cities: List<CityWeatherData> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed class MainScreenIntent {
    data class AddCity(val locationKey: String) : MainScreenIntent()
    data class DeleteCity(val id: Int) : MainScreenIntent()
    data class UpdateWeather(val cityId: Int) : MainScreenIntent()
    object DismissError : MainScreenIntent()
} 