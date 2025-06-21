package com.tydm.weatherApp.data.local.dao

import androidx.room.*
import com.tydm.weatherApp.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    // Cities
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity): Long

    @Query("DELETE FROM cities WHERE id = :id")
    suspend fun deleteCity(id: Int)

    @Update
    suspend fun updateCity(city: CityEntity)

    @Query("SELECT * FROM cities")
    fun getCities(): Flow<List<CityEntity>>

    @Query("SELECT * FROM cities WHERE id = :id")
    suspend fun getCityById(id: Int): CityEntity?

    // Current Weather
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM current_weather WHERE cityId = :cityId")
    suspend fun deleteWeather(cityId: Int)

    @Query("SELECT * FROM current_weather WHERE cityId = :cityId")
    fun getWeather(cityId: Int): Flow<WeatherEntity?>

    // Daily Forecast
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(forecast: List<DailyForecastEntity>)

    @Query("DELETE FROM daily_forecast WHERE cityId = :cityId")
    suspend fun deleteDailyForecast(cityId: Int)

    @Query("SELECT * FROM daily_forecast WHERE cityId = :cityId ORDER BY date ASC")
    fun getDailyForecast(cityId: Int): Flow<List<DailyForecastEntity>>

    // Hourly Forecast
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(forecast: List<HourlyForecastEntity>)

    @Query("DELETE FROM hourly_forecast WHERE cityId = :cityId")
    suspend fun deleteHourlyForecast(cityId: Int)

    @Query("SELECT * FROM hourly_forecast WHERE cityId = :cityId ORDER BY dateTime ASC")
    fun getHourlyForecast(cityId: Int): Flow<List<HourlyForecastEntity>>

    // Atomic update methods
    @Transaction
    suspend fun updateDailyForecast(cityId: Int, forecast: List<DailyForecastEntity>) {
        deleteDailyForecast(cityId)
        insertDailyForecast(forecast)
    }

    @Transaction
    suspend fun updateHourlyForecast(cityId: Int, forecast: List<HourlyForecastEntity>) {
        deleteHourlyForecast(cityId)
        insertHourlyForecast(forecast)
    }

    @Transaction
    suspend fun updateWeatherData(cityId: Int, weather: WeatherEntity) {
        deleteWeather(cityId)
        insertWeather(weather)
    }
} 