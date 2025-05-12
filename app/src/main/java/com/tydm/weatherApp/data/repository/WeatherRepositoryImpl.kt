package com.tydm.weatherApp.data.repository

import com.tydm.weatherApp.data.local.dao.WeatherDao
import com.tydm.weatherApp.data.mapper.*
import com.tydm.weatherApp.data.weatherapi.AccuweatherApi
import com.tydm.weatherApp.domain.model.*
import com.tydm.weatherApp.domain.repository.WeatherRepository
import com.tydm.weatherApp.util.toWeatherError
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherApi: AccuweatherApi
) : WeatherRepository {

    override suspend fun addCity(locationKey: String, language: String): WeatherResult<Unit> {
        return try {
            val cityResponse = weatherApi.getCityInfoByLocationKey(locationKey, language).body()
                ?: throw IllegalStateException("Empty city response")
            val cityId = weatherDao.insertCity(cityResponse.toEntity()).toInt()

            val dailyForecast = weatherApi.getDailyForecast(locationKey, language).body()
                ?: throw IllegalStateException("Empty daily forecast response")
            weatherDao.insertDailyForecast(dailyForecast.toDailyForecastEntityList(cityId))

            val currentWeather = weatherApi.getCurrentWeather(locationKey, language).body()
                ?: throw IllegalStateException("Empty weather response")
            weatherDao.insertWeather(currentWeather.toWeatherEntity(
                cityId,
                dailyForecast.dailyForecasts?.get(0)?.day?.precipitationProbability?:0))

            val hourlyForecast = weatherApi.getHourlyForecast(locationKey, language).body()
                ?: throw IllegalStateException("Empty hourly forecast response")
            weatherDao.insertHourlyForecast(hourlyForecast.toHourlyForecastEntityList(cityId))

            WeatherResult.Success(Unit)
        } catch (e: Exception) {
            WeatherResult.Error(e.toWeatherError())
        }
    }

    override suspend fun deleteCity(id: Int): WeatherResult<Unit> {
        return try {
            weatherDao.deleteCity(id)
            WeatherResult.Success(Unit)
        } catch (e: Exception) {
            WeatherResult.Error(WeatherError.DatabaseError(e))
        }
    }

    override suspend fun updateWeather(cityId: Int, language: String): WeatherResult<Unit> {
        return try {
            val cityEntity = weatherDao.getCityById(cityId) 
                ?: throw WeatherError.LocationError("Город не найден")

            val dailyForecast = weatherApi.getDailyForecast(cityEntity.locationKey, language).body()
                ?: throw IllegalStateException("Empty daily forecast response")
            weatherDao.insertDailyForecast(dailyForecast.toDailyForecastEntityList(cityId))

            val currentWeather = weatherApi.getCurrentWeather(cityEntity.locationKey, language).body()
                ?: throw IllegalStateException("Empty weather response")
            weatherDao.insertWeather(currentWeather.toWeatherEntity(
                cityId,
                dailyForecast.dailyForecasts?.get(0)?.day?.precipitationProbability?:0))

            val hourlyForecast = weatherApi.getHourlyForecast(cityEntity.locationKey, language).body()
                ?: throw IllegalStateException("Empty hourly forecast response")
            weatherDao.insertHourlyForecast(hourlyForecast.toHourlyForecastEntityList(cityId))

            WeatherResult.Success(Unit)
        } catch (e: Exception) {
            WeatherResult.Error(e.toWeatherError())
        }
    }

    override fun getCities(): Flow<WeatherResult<List<City>>> = flow {
        emit(WeatherResult.Loading)
        try {
            weatherDao.getCities()
                .map { entities -> entities.map { it.toDomain() } }
                .collect { cities ->
                    emit(WeatherResult.Success(cities))
                }
        } catch (e: Exception) {
            emit(WeatherResult.Error(WeatherError.DatabaseError(e)))
        }
    }

    override fun getCurrentWeather(cityId: Int): Flow<WeatherResult<Weather?>> = flow {
        emit(WeatherResult.Loading)
        try {
            weatherDao.getWeather(cityId)
                .map { entity -> entity?.toDomain() }
                .collect { weather ->
                    emit(WeatherResult.Success(weather))
                }
        } catch (e: Exception) {
            emit(WeatherResult.Error(WeatherError.DatabaseError(e)))
        }
    }

    override fun getDailyForecast(cityId: Int): Flow<WeatherResult<List<DailyForecast>>> = flow {
        emit(WeatherResult.Loading)
        try {
            weatherDao.getDailyForecast(cityId)
                .map { entities -> entities.map { it.toDomain() } }
                .collect { forecast ->
                    emit(WeatherResult.Success(forecast))
                }
        } catch (e: Exception) {
            emit(WeatherResult.Error(WeatherError.DatabaseError(e)))
        }
    }

    override fun getHourlyForecast(cityId: Int): Flow<WeatherResult<List<HourlyForecast>>> = flow {
        emit(WeatherResult.Loading)
        try {
            weatherDao.getHourlyForecast(cityId)
                .map { entities -> entities.map { it.toDomain() } }
                .collect { forecast ->
                    emit(WeatherResult.Success(forecast))
                }
        } catch (e: Exception) {
            emit(WeatherResult.Error(WeatherError.DatabaseError(e)))
        }
    }
} 