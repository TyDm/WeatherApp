package com.tydm.weatherApp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "current_weather")
data class CurrentWeather(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityId: Int,
    val cityName: String,
    val gmtOffset: Int,
    val updateTime: Date,
    val country: String,
    val region: String,
    val temperature: Float,
    val feelsLikeTemp: Float,
    val humidity: Int,
    val windMs: Int,
    val conditionText: String,
    val conditionCode: Int,
    val atmPrecipitation: Int,
)