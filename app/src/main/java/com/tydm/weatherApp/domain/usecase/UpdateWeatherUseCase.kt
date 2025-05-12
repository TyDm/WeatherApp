package com.tydm.weatherApp.domain.usecase

import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.repository.WeatherRepository
import javax.inject.Inject

class UpdateWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int, language: String): WeatherResult<Unit> =
        repository.updateWeather(cityId, language)
}