package com.tydm.weatherApp.data.repository

import android.util.Log
import com.tydm.weatherApp.data.local.dao.WeatherDao
import com.tydm.weatherApp.data.mapper.toDailyForecastEntityList
import com.tydm.weatherApp.data.mapper.toDomain
import com.tydm.weatherApp.data.mapper.toEntity
import com.tydm.weatherApp.data.mapper.toHourlyForecastEntityList
import com.tydm.weatherApp.data.mapper.toSearchItemList
import com.tydm.weatherApp.data.mapper.toWeatherEntity
import com.tydm.weatherApp.data.weatherapi.AccuWeatherLanguages
import com.tydm.weatherApp.data.weatherapi.AccuweatherApi
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.SearchItem
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.domain.model.WeatherError
import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.repository.WeatherRepository
import com.tydm.weatherApp.util.toWeatherError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherApi: AccuweatherApi,
    private val weatherLanguages: AccuWeatherLanguages,
) : WeatherRepository {

    override suspend fun addCity(locationKey: String): WeatherResult<Int> {
        return try {
            val language = weatherLanguages.language
            val cityResponse = weatherApi.getCityInfoByLocationKey(locationKey, language)
            if (cityResponse.body() == null) throw HttpException(cityResponse)
            val currentCities = weatherDao.getCities().first()
            val maxOrder = currentCities.maxOfOrNull { it.order } ?: -1
            val city = cityResponse.body()!!.toEntity(language, maxOrder + 1)
            val cityId = weatherDao.insertCity(city).toInt()

            updateWeather(cityId, locationKey, language)

            WeatherResult.Success(cityId)
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
                    weatherApi.getCityInfoByLocationKey(cityEntity.locationKey, language)
                if (cityResponse.body() == null) throw HttpException(cityResponse)
                weatherDao.updateCity(cityResponse.body()!!.toEntity(language, cityEntity.id))
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
            val dailyForecastResponse = weatherApi.getDailyForecast(locationKey, language)
            if (dailyForecastResponse.body() == null) throw HttpException(dailyForecastResponse)
            weatherDao.updateDailyForecast(cityId, dailyForecastResponse.body()!!.toDailyForecastEntityList(cityId))
            
            val currentWeatherResponse =
                weatherApi.getCurrentWeather(locationKey, language)
            if (currentWeatherResponse.body() == null) throw HttpException(currentWeatherResponse)
            weatherDao.updateWeatherData(
                cityId,
                currentWeatherResponse.body()!!.toWeatherEntity(
                    cityId,
                    dailyForecastResponse.body()!!.dailyForecasts?.firstOrNull()?.day?.precipitationProbability ?: 0
                )
            )
            
            val hourlyForecastResponse =
                weatherApi.getHourlyForecast(locationKey, language)
            if (hourlyForecastResponse.body() == null) throw HttpException(hourlyForecastResponse)
            weatherDao.updateHourlyForecast(cityId, hourlyForecastResponse.body()!!.toHourlyForecastEntityList(cityId))

            WeatherResult.Success(Unit)
        } catch (e: Exception) {
            WeatherResult.Error(e.toWeatherError())
        }
    }

    override suspend fun deleteCity(id: Int): WeatherResult<Unit> {
        return try {
            weatherDao.deleteCity(id)
            normalizeCityOrder()
            WeatherResult.Success(Unit)
        } catch (e: Exception) {
            WeatherResult.Error(WeatherError.DatabaseError(e))
        }
    }

    override fun getCities(): Flow<WeatherResult<List<City>>> = flow {
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

    override suspend fun searchCity(query: String): WeatherResult<List<SearchItem>> {
        try {
            val searchCityResponse = weatherApi.getCitiesListByName(
                cityName = query,
                language = weatherLanguages.language)
            if (searchCityResponse.body() == null) {
                Log.d("Exception","Exception on searchCity")
                throw HttpException(searchCityResponse)
            }
            return WeatherResult.Success(searchCityResponse.body()!!.toSearchItemList())
        } catch (e: Exception) {
            return WeatherResult.Error(e.toWeatherError())
        }
    }


    private suspend fun moveCityToTopInternal(cityId: Int) {
        val cities = weatherDao.getCities().map { it }.first()
        val target = cities.find { it.id == cityId } ?: return
        val updatedCities = cities.map {
            when {
                it.id == cityId -> it.copy(order = 0)
                it.order < target.order -> it.copy(order = it.order + 1)
                else -> it
            }
        }
        updatedCities.forEach { weatherDao.updateCity(it) }
    }

    override suspend fun moveCityToTop(cityId: Int): WeatherResult<Unit> {
        return try {
            moveCityToTopInternal(cityId)
            WeatherResult.Success(Unit)
        } catch (e: Exception) {
            WeatherResult.Error(e.toWeatherError())
        }
    }

    private suspend fun normalizeCityOrder() {
        val cities = weatherDao.getCities().first()
        cities.forEachIndexed { index, city ->
            if (city.order != index) {
                weatherDao.updateCity(city.copy(order = index))
            }
        }
    }
}