package com.tydm.weatherApp.domain.usecase

import com.tydm.weatherApp.domain.model.SearchItem
import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.repository.WeatherRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(query: String): WeatherResult<List<SearchItem>>  =
        repository.searchCity(query)
}