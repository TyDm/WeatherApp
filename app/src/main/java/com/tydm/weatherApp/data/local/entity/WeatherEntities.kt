package com.tydm.weatherApp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val locationKey: String,
    val name: String,
    val country: String,
    val administrativeArea: String,
    val gmtOffset: Int,
    val languageCode: String,
    val order: Int = 0
)

@Entity(
    tableName = "current_weather",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "cityId", index = true)
    val cityId: Int,
    val observationTime: Long,
    val updateTime: Long = System.currentTimeMillis(),
    val temperatureMetric: Int,
    val temperatureImperial: Int,
    val realFeelTemperatureMetric: Int,
    val realFeelTemperatureImperial: Int,
    val humidity: Int,
    val windMetric: Int,
    val windImperial: Float,
    val conditionText: String,
    val conditionCode: Int,
    val atmPrecipitation: Int,
    val mobileLink: String
)

@Entity(
    tableName = "daily_forecast",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DailyForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "cityId", index = true)
    val cityId: Int,
    val date: Long,
    val temperatureMinImperial: Int,
    val temperatureMaxImperial: Int,
    val temperatureMinMetric: Int,
    val temperatureMaxMetric: Int,
    val conditionText: String,
    val conditionCode: Int,
    val mobileLink: String
)

@Entity(
    tableName = "hourly_forecast",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HourlyForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "cityId", index = true)
    val cityId: Int,
    val dateTime: Long,
    val temperatureMetric: Float,
    val temperatureImperial: Int,
    val conditionText: String,
    val conditionCode: Int,
    val mobileLink: String
) 