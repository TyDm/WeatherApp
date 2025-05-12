package com.tydm.weatherApp.domain.repository

import com.tydm.weatherApp.domain.model.*
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun addCity(locationKey: String, language: String): WeatherResult<Unit>
    suspend fun deleteCity(id: Int): WeatherResult<Unit>
    suspend fun updateWeather(cityId: Int, language: String): WeatherResult<Unit>
    
    fun getCities(): Flow<WeatherResult<List<City>>>
    fun getCurrentWeather(cityId: Int): Flow<WeatherResult<Weather?>>
    fun getDailyForecast(cityId: Int): Flow<WeatherResult<List<DailyForecast>>>
    fun getHourlyForecast(cityId: Int): Flow<WeatherResult<List<HourlyForecast>>>
} 