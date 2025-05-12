package com.tydm.weatherApp.data.weatherapi


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AccuweatherApi {

    @GET("locations/v1/cities/autocomplete?apikey=$KEY")
    suspend fun getCitiesListByName(
        @Query("q") cityName: String,
        @Query("language") language: String
    ): Response<AutocompleteSearchResponse>

    @GET("locations/v1/{locationKey}?apikey=$KEY")
    suspend fun getCityInfoByLocationKey(
        @Path("locationKey") locationKey: String,
        @Query("language") language: String
    ): Response<CityResponse>

    @GET("currentconditions/v1/{locationKey}?apikey=$KEY&details=true")
    suspend fun getCurrentWeather(
        @Path("locationKey") locationKey: String,
        @Query("language") language: String
    ): Response<List<CurrentWeatherItem>>

    @GET("forecasts/v1/daily/5day/{locationKey}?apikey=$KEY&details=true")
    suspend fun getDailyForecast(
        @Path("locationKey") locationKey: String,
        @Query("language") language: String
    ): Response<DailyForecastResponse>

    @GET("forecasts/v1/hourly/12hour/{locationKey}?apikey=$KEY")
    suspend fun getHourlyForecast(
        @Path("locationKey") locationKey: String,
        @Query("language") language: String
    ): Response<List<HourlyForecastResponseItem>>

    companion object {
        private const val KEY = "2FXHaoGutYQIyfep3DyqedVbXOMgJAmH"
    }
}