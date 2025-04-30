package com.tydm.weatherApp.data

import com.tydm.weatherApp.data.mapper.toCurrentWeather
import com.tydm.weatherApp.data.mapper.toHourForecastList
import com.tydm.weatherApp.data.room.CurrentWeather
import com.tydm.weatherApp.data.room.HourForecast
import com.tydm.weatherApp.data.room.WeatherDao
import com.tydm.weatherApp.data.weatherapi.WeatherApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

sealed class NetworkResult{
    class Success: NetworkResult()
    data class Error(val error: String): NetworkResult()
    class Loading(): NetworkResult()
}

@Singleton
class Repository @Inject constructor(
    val weatherDao: WeatherDao,
    val weatherApi: WeatherApi
){

    fun refreshCityWeather(id:Int, cityId: Int): Flow<NetworkResult> = flow{
        emit(NetworkResult.Loading())
        try {
            val response = weatherApi.getForecastWeather(cityId = "id:$cityId")
            if (response.isSuccessful && response.body() != null) {
                response.body()?.let { forecastWeatherResponse ->
                    updateCityWeather(
                        id = id,
                        cityId = cityId,
                        currentWeather = forecastWeatherResponse.toCurrentWeather(cityId),
                        listForecast = forecastWeatherResponse.toHourForecastList(cityId))
                }
                emit(NetworkResult.Success())
            }
            else emit(NetworkResult.Error(response.errorBody().toString()))
        }
        catch (e: Exception){
            e.printStackTrace()
            emit(NetworkResult.Error("Error loading"))
        }
    }

    fun addCity(cityId: Int): Flow<NetworkResult> = flow{
        emit(NetworkResult.Loading())
        try {
            val response = weatherApi.getForecastWeather(cityId = "id:$cityId")
            if (response.isSuccessful && response.body() != null) {
                response.body()?.let { forecastWeatherResponse ->
                    weatherDao.insertCurrentWeather(forecastWeatherResponse.toCurrentWeather(cityId))
                    weatherDao.insertListForecast(forecastWeatherResponse.toHourForecastList(cityId))
                }
                emit(NetworkResult.Success())
            }
            else emit(NetworkResult.Error(response.errorBody().toString()))
        }
        catch (e: Exception){
            e.printStackTrace()
            emit(NetworkResult.Error("Error loading"))
        }

    }
    suspend fun removeCity(currentWeather: CurrentWeather){
        weatherDao.deleteCurrentWeather(currentWeather)
        weatherDao.deleteHourForecast(currentWeather.cityId)
    }
    suspend fun removeCity(cityId: Int){
        weatherDao.deleteCurrentWeather(cityId)
        weatherDao.deleteHourForecast(cityId)
    }
    suspend fun updateCityWeather(
        id:Int,
        cityId:Int,
        currentWeather: CurrentWeather,
        listForecast: List<HourForecast>){
        weatherDao.updateCurrentWeather(
            id = id,
            cityName = currentWeather.cityName,
            updateTime = currentWeather.updateTime,
            temperature = currentWeather.temperature,
            feelsLikeTemp = currentWeather.feelsLikeTemp,
            humidity = currentWeather.humidity,
            windMs = currentWeather.windMs,
            conditionText = currentWeather.conditionText,
            conditionCode = currentWeather.conditionCode,
            atmPrecipitation = currentWeather.atmPrecipitation
        )
        weatherDao.deleteHourForecast(cityId)
        weatherDao.insertListForecast(listForecast)
    }
    fun getAllCities(): Flow<List<CurrentWeather>>{
        return weatherDao.getAllCurrentWeather()
    }

    fun getAllHourForecast(): Flow<List<HourForecast>>{
        return weatherDao.getAllHourForecast()
    }

}