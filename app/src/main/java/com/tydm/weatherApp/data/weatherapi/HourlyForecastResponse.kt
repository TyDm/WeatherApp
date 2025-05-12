package com.tydm.weatherApp.data.weatherapi

import com.google.gson.annotations.SerializedName

data class HourlyForecastResponseItem(

    @field:SerializedName("Temperature")
    val temperature: HourlyTemperature? = null,

    @field:SerializedName("HasPrecipitation")
    val hasPrecipitation: Boolean? = null,

    @field:SerializedName("EpochDateTime")
    val epochDateTime: Long? = null,

    @field:SerializedName("IsDaylight")
    val isDaylight: Boolean? = null,

    @field:SerializedName("DateTime")
    val dateTime: String? = null,

    @field:SerializedName("IconPhrase")
    val iconPhrase: String? = null,

    @field:SerializedName("WeatherIcon")
    val weatherIcon: Int? = null,

    @field:SerializedName("MobileLink")
    val mobileLink: String? = null
)

data class HourlyTemperature(

    @field:SerializedName("Value")
    val value: Double? = null,
)