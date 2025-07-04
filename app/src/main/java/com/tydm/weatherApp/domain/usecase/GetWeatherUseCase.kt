package com.tydm.weatherApp.domain.usecase

import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    fun getCities(): Flow<WeatherResult<List<City>>> = 
        repository.getCities()
    
    fun getCurrentWeather(cityId: Int): Flow<WeatherResult<Weather?>> =
        repository.getCurrentWeather(cityId)
    
    fun getDailyForecast(cityId: Int): Flow<WeatherResult<List<DailyForecast>>> =
        repository.getDailyForecast(cityId)
    
    fun getHourlyForecast(cityId: Int): Flow<WeatherResult<List<HourlyForecast>>> =
        repository.getHourlyForecast(cityId)

    suspend fun moveCityToTop(cityId: Int) = repository.moveCityToTop(cityId)
} 