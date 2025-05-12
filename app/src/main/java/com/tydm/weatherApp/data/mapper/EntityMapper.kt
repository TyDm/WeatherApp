package com.tydm.weatherApp.data.mapper

import com.tydm.weatherApp.data.local.entity.CityEntity
import com.tydm.weatherApp.data.local.entity.DailyForecastEntity
import com.tydm.weatherApp.data.local.entity.HourlyForecastEntity
import com.tydm.weatherApp.data.local.entity.WeatherEntity
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.Weather

fun CityEntity.toDomain() = City(
    id = id,
    locationKey = locationKey,
    name = name,
    country = country,
    administrativeArea = administrativeArea,
    gmtOffset = gmtOffset
)

fun WeatherEntity.toDomain() = Weather(
    id = id,
    cityId = cityId,
    observationTime = observationTime,
    updateTime = updateTime,
    temperatureMetric = temperatureMetric,
    temperatureImperial = temperatureImperial,
    realFeelTemperatureMetric = realFeelTemperatureMetric,
    realFeelTemperatureImperial = realFeelTemperatureImperial,
    humidity = humidity,
    windMetric = windMetric,
    windImperial = windImperial,
    conditionText = conditionText,
    conditionCode = conditionCode,
    atmPrecipitation = atmPrecipitation,
    mobileLink = mobileLink
)

fun DailyForecastEntity.toDomain() = DailyForecast(
    id = id,
    cityId = cityId,
    date = date,
    temperatureMinImperial = temperatureMinImperial,
    temperatureMaxImperial = temperatureMaxImperial,
    temperatureMinMetric = temperatureMinMetric,
    temperatureMaxMetric = temperatureMaxMetric,
    conditionText = conditionText,
    conditionCode = conditionCode,
    mobileLink = mobileLink
)

fun HourlyForecastEntity.toDomain() = HourlyForecast(
    id = id,
    cityId = cityId,
    dateTime = dateTime,
    temperatureMetric = temperatureMetric,
    temperatureImperial = temperatureImperial,
    conditionText = conditionText,
    conditionCode = conditionCode,
    mobileLink = mobileLink
)
