package com.tydm.weatherApp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(hourForecast: HourForecast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)

    @Query("delete from current_weather")
    suspend fun clearAllCurrentWeather()

    @Query("delete from hour_forecast")
    suspend fun clearAllHourForecast()

    @Query("select * from hour_forecast")
    fun getAllHourForecast(): Flow<List<HourForecast>>

    @Query("select * from current_weather")
    fun getAllCurrentWeather(): Flow<List<CurrentWeather>>

}