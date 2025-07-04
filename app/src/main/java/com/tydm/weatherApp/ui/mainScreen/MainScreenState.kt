package com.tydm.weatherApp.ui.mainScreen
//

import com.tydm.weatherApp.domain.model.SearchItem
import com.tydm.weatherApp.ui.model.CityWeatherData

data class MainScreenState(
    val cities: List<CityWeatherData> = emptyList(),
    val searchCitiesList: List<SearchItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
)

