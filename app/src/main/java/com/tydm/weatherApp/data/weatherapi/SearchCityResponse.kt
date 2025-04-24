package com.tydm.weatherApp.data.weatherapi

import com.google.gson.annotations.SerializedName

data class SearchCityResponse(
	val cityList: List<SearchCityItem?>? = null
)

data class SearchCityItem(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("region")
	val region: String? = null,
)
