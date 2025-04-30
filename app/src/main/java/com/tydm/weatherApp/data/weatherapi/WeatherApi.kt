package com.tydm.weatherApp.data.weatherapi


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET(FORECAST_URL)
    suspend fun getForecastWeather(
        @Query("q") cityId:String = "id:2145091",
        @Query("days") days: Int = 2,
        @Query("lang") lang: String = "ru",
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ): Response<ForecastWeatherResponse>

    @GET(SEARCH_URL)
    suspend fun searchCityByName(
        @Query("q") cityName:String
    ): SearchCityResponse

    companion object{
        private const val KEY="04c6e231382e41a992d73655252404"
        private const val SEARCH_URL= "search.json?key=$KEY"
        private const val FORECAST_URL="forecast.json?key=$KEY"
    }
}