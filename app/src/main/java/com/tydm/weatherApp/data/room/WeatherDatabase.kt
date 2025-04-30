package com.tydm.weatherApp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [HourForecast::class, CurrentWeather::class], version = 1)
@TypeConverters(Converters::class)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
}