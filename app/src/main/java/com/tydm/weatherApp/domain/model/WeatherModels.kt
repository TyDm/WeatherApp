package com.tydm.weatherApp.domain.model

data class Weather(
    val id: Long = 0,
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

data class City(
    val id: Int = 0,
    val locationKey: String,
    val name: String,
    val country: String,
    val administrativeArea: String,
    val gmtOffset: Int
)

data class DailyForecast(
    val id: Long = 0,
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

data class HourlyForecast(
    val id: Long = 0,
    val cityId: Int,
    val dateTime: Long,
    val temperatureMetric: Float,
    val temperatureImperial: Int,
    val conditionText: String,
    val conditionCode: Int,
    val mobileLink: String
)

data class SearchItem(
    val key: String,
    val name: String,
    val administrativeArea: String,
    val country: String,
    val type: String,
    val rank: Int
)