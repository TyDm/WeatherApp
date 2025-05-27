package com.tydm.weatherApp.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetWeatherUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var useCase: GetWeatherUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetWeatherUseCase(repository)
    }

    @Test
    fun `getCurrentWeather returns weather data from repository`() = runTest {
        // Given
        val cityId = 1
        val weather = Weather(
         cityId = cityId,
         observationTime = System.currentTimeMillis(),
         updateTime = System.currentTimeMillis(),
         temperatureMetric = 0,
         temperatureImperial = 0,
         realFeelTemperatureMetric = 0,
         realFeelTemperatureImperial = 0,
         humidity = 0,
         windMetric = 0,
         windImperial = 0f,
         conditionText = "Clear",
         conditionCode = 0,
         atmPrecipitation = 0,
         mobileLink = ""
        )
        every {
            repository.getCurrentWeather(cityId)
        } returns flowOf(WeatherResult.Success(weather))

        // When & Then
        useCase.getCurrentWeather(cityId).test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
            assertThat((result as WeatherResult.Success).data?.conditionText)
                .isEqualTo("Clear")
            awaitComplete()
        }
    }

    @Test
    fun `getDailyForecast returns forecast data from repository`() = runTest {
        // Given
        val cityId = 1
        val forecast = listOf(
            DailyForecast(
                id = 1,
                cityId = cityId,
                date = System.currentTimeMillis(),
                temperatureMaxImperial = 0,
                temperatureMaxMetric = 0,
                temperatureMinMetric = 0,
                temperatureMinImperial = 0,
                conditionText = "Clear",
                conditionCode = 0,
                mobileLink = ""
            )
        )
        every {
            repository.getDailyForecast(cityId)
        } returns flowOf(WeatherResult.Success(forecast))

        // When & Then
        useCase.getDailyForecast(cityId).test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
            assertThat((result as WeatherResult.Success).data)
                .hasSize(1)
            assertThat(result.data[0].conditionText)
                .isEqualTo("Clear")
            awaitComplete()
        }
    }

    @Test
    fun `getHourlyForecast returns forecast data from repository`() = runTest {
        // Given
        val cityId = 1
        val forecast = listOf(
            HourlyForecast(
                id = 1,
                cityId = cityId,
                dateTime = System.currentTimeMillis(),
                temperatureMetric = 0f,
                temperatureImperial = 0,
                conditionText = "Clear",
                conditionCode = 0,
                mobileLink = ""
            )
        )
        every {
            repository.getHourlyForecast(cityId)
        } returns flowOf(WeatherResult.Success(forecast))

        // When & Then
        useCase.getHourlyForecast(cityId).test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
            assertThat((result as WeatherResult.Success).data)
                .hasSize(1)
            assertThat(result.data[0].conditionText)
                .isEqualTo("Clear")
            awaitComplete()
        }
    }

    @Test
    fun `getCities returns cities from repository`() = runTest {
        // Given
        val cities = listOf(
            City(
                id = 1,
                locationKey = "123456",
                name = "Moscow",
                country = "Russia",
                administrativeArea = "Moscow",
                gmtOffset = 3
            )
        )
        every {
            repository.getCities()
        } returns flowOf(WeatherResult.Success(cities))

        // When & Then
        useCase.getCities().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
            assertThat((result as WeatherResult.Success).data)
                .hasSize(1)
            assertThat(result.data[0].name)
                .isEqualTo("Moscow")
            awaitComplete()
        }
    }
}