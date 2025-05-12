package com.tydm.weatherApp.data.weatherapi

import com.google.gson.annotations.SerializedName

data class CityResponse(

	@field:SerializedName("Version")
	val version: Int? = null,

	@field:SerializedName("Key")
	val key: String? = null,

	@field:SerializedName("Type")
	val type: String? = null,

	@field:SerializedName("Rank")
	val rank: Int? = null,

	@field:SerializedName("LocalizedName")
	val localizedName: String? = null,

	@field:SerializedName("Region")
	val region: Region? = null,

	@field:SerializedName("Country")
	val country: CityCountry? = null,

	@field:SerializedName("AdministrativeArea")
	val administrativeArea: AdministrativeAreaInfo? = null,

	@field:SerializedName("TimeZone")
	val timeZone: TimeZone? = null,
)
data class TimeZone(

	@field:SerializedName("NextOffsetChange")
	val nextOffsetChange: Any? = null,

	@field:SerializedName("GmtOffset")
	val gmtOffset: Int? = null,

	@field:SerializedName("Code")
	val code: String? = null,

	@field:SerializedName("IsDaylightSaving")
	val isDaylightSaving: Boolean? = null,

	@field:SerializedName("Name")
	val name: String? = null
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

data class Region(

	@field:SerializedName("LocalizedName")
	val localizedName: String? = null,
)
