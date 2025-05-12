package com.tydm.weatherApp.data.weatherapi

import com.google.gson.annotations.SerializedName

data class CurrentWeatherItem(

    @field:SerializedName("LocalObservationDateTime")
    val localObservationDateTime: String? = null,

    @field:SerializedName("EpochTime")
    val epochTime: Long? = null,

    @field:SerializedName("Temperature")
    val temperature: Temperature? = null,

    @field:SerializedName("RealFeelTemperature")
    val realFeelTemperature: RealFeelTemperature? = null,

    @field:SerializedName("RelativeHumidity")
    val relativeHumidity: Int? = null,

    @field:SerializedName("Wind")
    val wind: Wind? = null,

    @field:SerializedName("WeatherText")
    val weatherText: String? = null,

    @field:SerializedName("WeatherIcon")
    val weatherIcon: Int? = null,

    @field:SerializedName("IsDayTime")
    val isDayTime: Boolean? = null,

    @field:SerializedName("CloudCover")
    val cloudCover: Int? = null,

    @field:SerializedName("MobileLink")
    val mobileLink: String? = null
)

data class Temperature(

    @field:SerializedName("Metric")
    val metric: Metric? = null,

    @field:SerializedName("Imperial")
    val imperial: Imperial? = null
)

data class RealFeelTemperature(

    @field:SerializedName("Metric")
    val metric: Metric? = null,

    @field:SerializedName("Imperial")
    val imperial: Imperial? = null
)

data class Wind(

    @field:SerializedName("Speed")
    val speed: Speed? = null,

    @field:SerializedName("Direction")
    val direction: Direction? = null
)

data class Speed(

    @field:SerializedName("Metric") // км/ч
    val metric: Metric? = null,

    @field:SerializedName("Imperial") //mi/h
    val imperial: Imperial? = null
)

data class Direction(

    @field:SerializedName("Degrees")
    val degrees: Int? = null,

    @field:SerializedName("Localized")
    val localized: String? = null
)

data class Imperial(

    @field:SerializedName("UnitType")
    val unitType: Int? = null,

    @field:SerializedName("Value")
    val value: Double? = null,

    @field:SerializedName("Unit")
    val unit: String? = null,

    @field:SerializedName("Phrase")
    val phrase: String? = null
)

data class Metric(

    @field:SerializedName("UnitType")
    val unitType: Int? = null,

    @field:SerializedName("Value")
    val value: Double? = null,

    @field:SerializedName("Unit")
    val unit: String? = null,

    @field:SerializedName("Phrase")
    val phrase: String? = null
)
