package com.tydm.weatherApp.data.weatherapi

import com.google.gson.annotations.SerializedName

data class CityResponse(

    @field:SerializedName("Key")
    val key: String? = null,

    @field:SerializedName("Type")
    val type: String? = null,

    @field:SerializedName("Rank")
    val rank: Int? = null,

    @field:SerializedName("LocalizedName")
    val localizedName: String? = null,

    @field:SerializedName("Country")
    val country: CityCountry? = null,

    @field:SerializedName("AdministrativeArea")
    val administrativeArea: AdministrativeAreaInfo? = null,

    @field:SerializedName("TimeZone")
    val timeZone: TimeZone? = null,
)

data class TimeZone(

    @field:SerializedName("GmtOffset")
    val gmtOffset: Int? = null
)

data class AdministrativeAreaInfo(

    @field:SerializedName("LocalizedType")
    val localizedType: String? = null,

    @field:SerializedName("LocalizedName")
    val localizedName: String? = null,
)

data class CityCountry(

    @field:SerializedName("LocalizedName")
    val localizedName: String? = null,
)
