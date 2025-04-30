package com.tydm.weatherApp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)

    @Delete
    suspend fun deleteCurrentWeather(currentWeather: CurrentWeather)

    @Query("delete from current_weather where cityId=:cityId")
    suspend fun deleteCurrentWeather(cityId: Int)

    @Query("delete from current_weather")
    suspend fun clearAllCurrentWeather()

    @Query("update current_weather set " +
            "cityName=:cityName, " +
            "updateTime=:updateTime, " +
            "temperature=:temperature, " +
            "feelsLikeTemp=:feelsLikeTemp, " +
            "humidity=:humidity, " +
            "windMs=:windMs, " +
            "conditionText=:conditionText, " +
            "conditionCode=:conditionCode, " +
            "atmPrecipitation=:atmPrecipitation" +
            " where id=:id")
    suspend fun updateCurrentWeather(
        id: Int,
        cityName: String,
        updateTime: Date,
        temperature: Float,
        feelsLikeTemp: Float,
        humidity: Int,
        windMs: Int,
        conditionText: String,
        conditionCode: Int,
        atmPrecipitation: Int
        )

    @Query("select * from current_weather")
    fun getAllCurrentWeather(): Flow<List<CurrentWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListForecast(listHourForecast: List<HourForecast>)

    @Query("delete from hour_forecast")
    suspend fun clearAllHourForecast()

    @Query("delete from hour_forecast where cityId=:cityId")
    suspend fun deleteHourForecast(cityId: Int)

    @Query("select * from hour_forecast")
    fun getAllHourForecast(): Flow<List<HourForecast>>



}