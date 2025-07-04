package com.tydm.weatherApp.data.weatherapi

import com.google.gson.annotations.SerializedName

data class AutocompleteSearchResponseItem(

	@field:SerializedName("AdministrativeArea")
	val administrativeArea: AdministrativeArea? = null,

	@field:SerializedName("Type")
	val type: String? = null,

	@field:SerializedName("Version")
	val version: Int? = null,

	@field:SerializedName("LocalizedName")
	val localizedName: String? = null,

	@field:SerializedName("Country")
	val country: Country? = null,

	@field:SerializedName("Rank")
	val rank: Int? = null,

	@field:SerializedName("Key")
	val key: String? = null
)

data class AdministrativeArea(

	@field:SerializedName("LocalizedName")
	val localizedName: String? = null,

	@field:SerializedName("ID")
	val iD: String? = null
)

data class Country(

	@field:SerializedName("LocalizedName")
	val localizedName: String? = null,

	@field:SerializedName("ID")
	val iD: String? = null
)
