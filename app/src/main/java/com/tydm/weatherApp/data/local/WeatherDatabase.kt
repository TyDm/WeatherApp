package com.tydm.weatherApp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tydm.weatherApp.data.local.dao.WeatherDao
import com.tydm.weatherApp.data.local.entity.CityEntity
import com.tydm.weatherApp.data.local.entity.WeatherEntity
import com.tydm.weatherApp.data.local.entity.DailyForecastEntity
import com.tydm.weatherApp.data.local.entity.HourlyForecastEntity

@Database(
    entities = [
        CityEntity::class,
        WeatherEntity::class,
        DailyForecastEntity::class,
        HourlyForecastEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
} 