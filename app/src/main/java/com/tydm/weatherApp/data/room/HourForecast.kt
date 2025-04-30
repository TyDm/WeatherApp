package com.tydm.weatherApp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "hour_forecast")
data class HourForecast(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityId: Int,
    val time: Date,
    val temp: Float
)