package com.tydm.weatherApp.domain.usecase

import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.repository.WeatherRepository
import javax.inject.Inject

class DeleteCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(id: Int): WeatherResult<Unit> =
        repository.deleteCity(id)
}