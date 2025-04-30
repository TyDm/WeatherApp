package com.tydm.weatherApp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeather(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityId: Int,
    val cityName: String,
    val gmtOffset: Int,
    val updateTime: Long,
    val country: String,
    val region: String,
    val temp: Float,
    val feelsLikeTemp: Float,
    val humidity: Int,
    val windMs: Int,
    val conditionText: String,
    val conditionCode: Int,
    val atmPrecipitation: Int,
)