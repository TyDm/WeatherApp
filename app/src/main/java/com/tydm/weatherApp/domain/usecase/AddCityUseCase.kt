package com.tydm.weatherApp.domain.usecase

import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.repository.WeatherRepository
import javax.inject.Inject

class AddCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(locationKey: String, language: String): WeatherResult<Unit> =
        repository.addCity(locationKey, language)
} 