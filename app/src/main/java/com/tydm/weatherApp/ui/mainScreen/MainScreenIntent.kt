package com.tydm.weatherApp.ui.mainScreen

sealed class MainScreenIntent {
    data class AddCity(val locationKey: String) : MainScreenIntent()
    data class DeleteCity(val id: Int) : MainScreenIntent()
    data class UpdateWeather(val cityId: Int) : MainScreenIntent()
    data class SearchCity(val cityName: String) : MainScreenIntent()
    object ClearEffect : MainScreenIntent()
    data class MoveCityToTop(val cityId: Int) : MainScreenIntent()
}