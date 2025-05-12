package com.tydm.weatherApp.data.weatherapi

import com.google.gson.annotations.SerializedName

data class DailyForecastResponse(

    @field:SerializedName("DailyForecasts")
    val dailyForecasts: List<DailyForecastsItem?>? = null
)

data class DailyForecastsItem(

    @field:SerializedName("Temperature")
    val temperature: DailyTemperature? = null,

    @field:SerializedName("EpochDate")
    val epochDate: Long? = null,

    @field:SerializedName("Day")
    val day: Day? = null,

    @field:SerializedName("Date")
    val date: String? = null,

    @field:SerializedName("MobileLink")
    val mobileLink: String? = null
)

data class DailyTemperature(

    @field:SerializedName("Minimum")
    val minimum: Minimum? = null,

    @field:SerializedName("Maximum")
    val maximum: Maximum? = null
)

data class Maximum(

    @field:SerializedName("Value")
    val value: Double? = null,
)

data class Minimum(

    @field:SerializedName("Value")
    val value: Double? = null,
)

data class Day(

    @field:SerializedName("HasPrecipitation")
    val hasPrecipitation: Boolean? = null,

    @field:SerializedName("IconPhrase")
    val iconPhrase: String? = null,

    @field:SerializedName("Icon")
    val icon: Int? = null,

    @field:SerializedName("PrecipitationProbability")
    val precipitationProbability: Int? = null,

    @field:SerializedName("PrecipitationType")
    val precipitationType: String? = null,

    @field:SerializedName("PrecipitationIntensity")
    val precipitationIntensity: String? = null
)
