package com.tydm.weatherApp.data.repository

import com.tydm.weatherApp.data.local.dao.WeatherDao
import com.tydm.weatherApp.data.mapper.toDailyForecastEntityList
import com.tydm.weatherApp.data.mapper.toDomain
import com.tydm.weatherApp.data.mapper.toEntity
import com.tydm.weatherApp.data.mapper.toHourlyForecastEntityList
import com.tydm.weatherApp.data.mapper.toWeatherEntity
import com.tydm.weatherApp.data.weatherapi.AccuWeatherLanguages
import com.tydm.weatherApp.data.weatherapi.AccuweatherApi
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.domain.model.WeatherError
import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.repository.WeatherRepository
import com.tydm.weatherApp.util.toWeatherError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherApi: AccuweatherApi,
    private val weatherLanguages: AccuWeatherLanguages,
) : WeatherRepository {

    override suspend fun addCity(locationKey: String): WeatherResult<Unit> {
        return try {
            val language = weatherLanguages.language
            val cityResponse = weatherApi.getCityInfoByLocationKey(locationKey, language).body()
                ?: throw IllegalStateException("Empty city response")
            val cityId = weatherDao.insertCity(cityResponse.toEntity(language)).toInt()

            updateWeather(cityId, locationKey, language)

            WeatherResult.Success(Unit)
        } catch (e: Exception) {
            WeatherResult.Error(e.toWeatherError())
        }
    }

    override suspend fun updateWeather(cityId: Int): WeatherResult<Unit> {
        return try {
            val language = weatherLanguages.language
            val cityEntity = weatherDao.getCityById(cityId)
                ?: throw WeatherError.LocationError("City not found")

            if (cityEntity.languageCode != language) {
                val cityResponse =
                    weatherApi.getCityInfoByLocationKey(cityEntity.locationKey, language).body()
                        ?: throw IllegalStateException("Empty city response")
                weatherDao.updateCity(cityResponse.toEntity(language, cityEntity.id))
            }
            updateWeather(cityId, cityEntity.locationKey, language)
        } catch (e: Exception) {
            WeatherResult.Error(e.toWeatherError())
        }
    }

    private suspend fun updateWeather(
        cityId: Int,
        locationKey: String,
        language: String
    ): WeatherResult<Unit> {
        return try {
            val dailyForecast = weatherApi.getDailyForecast(locationKey, language).body()
                ?: throw IllegalStateException("Empty daily forecast response")
            weatherDao.deleteDailyForecast(cityId)
            weatherDao.insertDailyForecast(dailyForecast.toDailyForecastEntityList(cityId))
            val currentWeather =
                weatherApi.getCurrentWeather(locationKey, language).body()
                    ?: throw IllegalStateException("Empty weather response")
            weatherDao.deleteWeather(cityId)
            weatherDao.insertWeather(
                currentWeather.toWeatherEntity(
                    cityId,
                    dailyForecast.dailyForecasts?.get(0)?.day?.precipitationProbability ?: 0
                )
            )
            val hourlyForecast =
                weatherApi.getHourlyForecast(locationKey, language).body()
                    ?: throw IllegalStateException("Empty hourly forecast response")
            weatherDao.deleteHourlyForecast(cityId)
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

    override fun getCurrentWeather(cityId: Int): Flow<WeatherResult<Weather>> = flow {
        emit(WeatherResult.Loading)
        try {
            weatherDao.getWeather(cityId)
                .map { entity -> entity?.toDomain() }
                .collect { weather ->
                    if (weather != null)
                        emit(WeatherResult.Success(weather))
                    else emit(WeatherResult.Error(WeatherError.DatabaseError(Exception("Database error"))))
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