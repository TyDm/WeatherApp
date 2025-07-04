package com.tydm.weatherApp.ui.mainScreen

sealed class MainScreenEffect {
    data class ScrollToCity(val cityId: Int) : MainScreenEffect()
    data class ShowError(val message: String) : MainScreenEffect()
    object ShowBottomSheet : MainScreenEffect()
}