package com.tydm.weatherApp.data.weatherapi

import com.google.gson.annotations.SerializedName

data class ForecastWeatherResponse(

	@field:SerializedName("current")
	val current: Current? = null,

	@field:SerializedName("location")
	val location: Location? = null,

	@field:SerializedName("forecast")
	val forecast: Forecast? = null
)

data class Location(

	@field:SerializedName("localtime") // Локальное время
	val localtime: String? = null,

	@field:SerializedName("country") // Страна
	val country: String? = null,

	@field:SerializedName("name") // Город
	val name: String? = null,

	@field:SerializedName("region") // Регион
	val region: String? = null,
)

data class Current(

	@field:SerializedName("last_updated") // Последнее обновление
	val lastUpdated: String? = null,

	@field:SerializedName("temp_c") // Температура
	val tempC: Any? = null,

	@field:SerializedName("feelslike_c") // Ощущаемая температура
	val feelslikeC: Any? = null,

	@field:SerializedName("is_day") // Светлое время суток
	val isDay: Int? = null,

	@field:SerializedName("humidity") // Влажность
	val humidity: Int? = null,

	@field:SerializedName("cloud")  // Облачность
	val cloud: Int? = null,

	@field:SerializedName("wind_kph") // Ветер в км/ч
	val windKph: Any? = null,

	@field:SerializedName("precip_mm") // Осадки
	val precipMm: Any? = null,

	@field:SerializedName("condition") // Условия погоды
	val condition: Condition? = null,
)

data class Condition(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("text")
	val text: String? = null
)

data class Forecast(

	@field:SerializedName("forecastday")
	val forecastday: List<ForecastdayItem?>? = null
)

data class ForecastdayItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("hour")
	val hour: List<HourItem?>? = null,

	@field:SerializedName("day")
	val day: Day? = null
)

data class Day(

	@field:SerializedName("avgtemp_c")
	val avgtempC: Any? = null,

	@field:SerializedName("daily_chance_of_snow")
	val dailyChanceOfSnow: Int? = null,

	@field:SerializedName("avgvis_miles")
	val avgvisMiles: Any? = null,

	@field:SerializedName("daily_will_it_rain")
	val dailyWillItRain: Int? = null,

	@field:SerializedName("mintemp_f")
	val mintempF: Any? = null,

	@field:SerializedName("totalprecip_in")
	val totalprecipIn: Any? = null,

	@field:SerializedName("totalsnow_cm")
	val totalsnowCm: Any? = null,

	@field:SerializedName("avghumidity") //
	val avghumidity: Int? = null,

	@field:SerializedName("condition") //
	val condition: Condition? = null,

	@field:SerializedName("maxwind_kph") //
	val maxwindKph: Any? = null,

	@field:SerializedName("maxwind_mph") //
	val maxwindMph: Any? = null,

	@field:SerializedName("daily_chance_of_rain")
	val dailyChanceOfRain: Int? = null,
)

data class HourItem(

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("will_it_snow")
	val willItSnow: Int? = null,

	@field:SerializedName("will_it_rain")
	val willItRain: Int? = null,

	@field:SerializedName("temp_c")
	val tempC: Any? = null,

	@field:SerializedName("condition")
	val condition: Condition? = null,

)



