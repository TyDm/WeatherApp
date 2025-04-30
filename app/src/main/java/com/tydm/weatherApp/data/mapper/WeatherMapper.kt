package com.tydm.weatherApp.data.mapper

import com.tydm.weatherApp.data.room.CurrentWeather
import com.tydm.weatherApp.data.room.HourForecast
import com.tydm.weatherApp.data.weatherapi.ForecastWeatherResponse
import java.util.Date
import kotlin.math.roundToInt

fun ForecastWeatherResponse.toCurrentWeather(cityId: Int): CurrentWeather {
    val dailyChanceOfSnow = forecast?.forecastday?.get(0)?.day?.dailyChanceOfSnow?:0
    val dailyChanceOfRain = forecast?.forecastday?.get(0)?.day?.dailyChanceOfRain?:0
    val atmPrecipitation = if (dailyChanceOfSnow >= dailyChanceOfRain)
        dailyChanceOfSnow else dailyChanceOfRain
    return CurrentWeather(
    cityId = cityId,
    cityName = location?.name ?: "",
    gmtOffset = 0, // TODO
    updateTime = Date(),
    country = location?.country ?: "",
    region = location?.region ?: "",
    temperature = current?.tempC ?: 0f,
    feelsLikeTemp = current?.feelslikeC ?: 0f,
    humidity = current?.humidity ?: 0,
    windMs = current?.windKph?.roundToInt() ?: 0,
    conditionText = current?.condition?.text ?: "",
    conditionCode = current?.condition?.code ?: 100,
    atmPrecipitation = atmPrecipitation
    )
}
fun ForecastWeatherResponse.toHourForecastList(cityId: Int): List<HourForecast>{
    val hourList = mutableListOf<HourForecast>()
    forecast?.forecastday?.forEach { dayItem ->
        dayItem?.hour?.forEach { hourItem ->
            hourList.add(HourForecast(
                cityId = cityId,
                time = Date(hourItem?.timeEpoch?:0L),
                temperature = hourItem?.tempC?:0f))
        }
    }
    return hourList.toList()
}