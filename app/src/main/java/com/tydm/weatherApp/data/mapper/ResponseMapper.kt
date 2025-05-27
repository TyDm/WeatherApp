package com.tydm.weatherApp.data.mapper

import com.tydm.weatherApp.data.local.entity.CityEntity
import com.tydm.weatherApp.data.local.entity.DailyForecastEntity
import com.tydm.weatherApp.data.local.entity.HourlyForecastEntity
import com.tydm.weatherApp.data.local.entity.WeatherEntity
import com.tydm.weatherApp.data.weatherapi.CityResponse
import com.tydm.weatherApp.data.weatherapi.CurrentWeatherItem
import com.tydm.weatherApp.data.weatherapi.DailyForecastResponse
import com.tydm.weatherApp.data.weatherapi.HourlyForecastResponseItem
import kotlin.math.roundToInt

private fun imperialTempToMetricInt(value: Double): Int = ((value - 32) / 1.8).roundToInt()
private fun imperialTempToMetricFloat(value: Double): Float = ((value - 32) / 1.8).toFloat()

fun CityResponse.toEntity(languageCode: String): CityEntity =
    if (key != null &&
        localizedName != null &&
        country?.localizedName != null &&
        administrativeArea?.localizedName != null &&
        timeZone?.gmtOffset != null
    )
        CityEntity(
            locationKey = key,
            name = localizedName,
            country = country.localizedName,
            administrativeArea = administrativeArea.localizedName,
            gmtOffset = timeZone.gmtOffset,
            languageCode = languageCode
        )
    else throw IllegalStateException("Invalid city response")

fun CityResponse.toEntity(languageCode: String, id: Int): CityEntity =
    toEntity(languageCode).copy(id = id)

fun List<CurrentWeatherItem>.toWeatherEntity(cityId: Int, atmPrecipitation: Int): WeatherEntity {
    val currentWeatherItem = firstOrNull() ?: throw IllegalStateException("Empty weather response")
    if (
        currentWeatherItem.epochTime != null &&
        currentWeatherItem.temperature?.imperial?.value != null &&
        currentWeatherItem.realFeelTemperature?.imperial?.value != null &&
        currentWeatherItem.wind?.speed?.metric?.value != null &&
        currentWeatherItem.wind.speed.imperial?.value != null &&
        currentWeatherItem.weatherText != null
    )
        return WeatherEntity(
            cityId = cityId,
            observationTime = currentWeatherItem.epochTime,
            updateTime = System.currentTimeMillis(),
            temperatureMetric = currentWeatherItem.temperature.metric?.value?.roundToInt()
                ?: imperialTempToMetricInt(currentWeatherItem.temperature.imperial.value),
            temperatureImperial = currentWeatherItem.temperature.imperial.value.roundToInt(),
            realFeelTemperatureMetric =
                currentWeatherItem.realFeelTemperature.metric?.value?.roundToInt()
                    ?: imperialTempToMetricInt(
                        currentWeatherItem.realFeelTemperature.imperial.value
                    ),
            realFeelTemperatureImperial =
                currentWeatherItem.realFeelTemperature.imperial.value.roundToInt(),
            humidity = currentWeatherItem.relativeHumidity ?: 0,
            windMetric = currentWeatherItem.wind.speed.metric.value.div(3.6f).roundToInt(),
            windImperial = currentWeatherItem.wind.speed.imperial.value.toFloat(),
            conditionText = currentWeatherItem.weatherText,
            conditionCode = currentWeatherItem.weatherIcon ?: 0,
            atmPrecipitation = atmPrecipitation,
            mobileLink = currentWeatherItem.mobileLink ?: ""
        )
    else throw IllegalStateException("Invalid weather response")
}

fun DailyForecastResponse.toDailyForecastEntityList(cityId: Int): List<DailyForecastEntity> {
    if (dailyForecasts.isNullOrEmpty()) throw IllegalStateException("Invalid daily forecast response")

    return dailyForecasts.mapNotNull { forecastItem ->
        if (forecastItem?.epochDate != null &&
            forecastItem.temperature?.minimum?.value != null &&
            forecastItem.temperature.maximum?.value != null &&
            forecastItem.day?.iconPhrase != null
        ) {
            DailyForecastEntity(
                cityId = cityId,
                date = forecastItem.epochDate,
                temperatureMinImperial = forecastItem.temperature.minimum.value.roundToInt(),
                temperatureMaxImperial = forecastItem.temperature.maximum.value.roundToInt(),
                temperatureMinMetric = imperialTempToMetricInt(forecastItem.temperature.minimum.value),
                temperatureMaxMetric = imperialTempToMetricInt(forecastItem.temperature.maximum.value),
                conditionText = forecastItem.day.iconPhrase,
                conditionCode = forecastItem.day.icon ?: 0,
                mobileLink = forecastItem.mobileLink ?: ""
            )
        } else null
    }
}

fun List<HourlyForecastResponseItem>.toHourlyForecastEntityList(cityId: Int): List<HourlyForecastEntity> =
    mapNotNull { item ->
        if (item.epochDateTime != null && item.temperature?.value != null && item.iconPhrase != null) {
            HourlyForecastEntity(
                cityId = cityId,
                dateTime = item.epochDateTime,
                temperatureMetric = imperialTempToMetricFloat(item.temperature.value),
                temperatureImperial = item.temperature.value.roundToInt(),
                conditionText = item.iconPhrase,
                conditionCode = item.weatherIcon ?: 0,
                mobileLink = item.mobileLink ?: ""
            )
        } else throw IllegalStateException("Invalid hourly forecast response")
    }