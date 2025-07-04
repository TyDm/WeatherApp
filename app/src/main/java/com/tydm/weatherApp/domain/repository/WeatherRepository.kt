package com.tydm.weatherApp.domain.repository

import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.SearchItem
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.domain.model.WeatherResult
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun addCity(locationKey: String): WeatherResult<Int>
    suspend fun deleteCity(id: Int): WeatherResult<Unit>
    suspend fun updateWeather(cityId: Int): WeatherResult<Unit>
    suspend fun searchCity(query: String): WeatherResult<List<SearchItem>>
    suspend fun moveCityToTop(cityId: Int): WeatherResult<Unit>

    fun getCities(): Flow<WeatherResult<List<City>>>
    fun getCurrentWeather(cityId: Int): Flow<WeatherResult<Weather?>>
    fun getDailyForecast(cityId: Int): Flow<WeatherResult<List<DailyForecast>>>
    fun getHourlyForecast(cityId: Int): Flow<WeatherResult<List<HourlyForecast>>>
} 