package com.tydm.weatherApp.domain.usecase

//import app.cash.turbine.test
//import com.google.common.truth.Truth.assertThat
//import com.tydm.weatherApp.domain.model.*
//import com.tydm.weatherApp.domain.repository.WeatherRepository
//import io.mockk.every
//import io.mockk.mockk
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Test
//
//class GetWeatherUseCaseTest {
//
//    private lateinit var repository: WeatherRepository
//    private lateinit var useCase: GetWeatherUseCase
//
//    @Before
//    fun setup() {
//        repository = mockk()
//        useCase = GetWeatherUseCase(repository)
//    }
//
//    @Test
//    fun `getCurrentWeather returns weather data from repository`() = runTest {
//        // Given
//        val cityId = 1
//        val weather = Weather(
//            id = 1,
//            cityId = cityId,
//            cityName = "Moscow",
//            temperature = 20.0,
//            condition = "Clear",
//            precipitationProbability = 0
//        )
//        every {
//            repository.getCurrentWeather(cityId)
//        } returns flowOf(WeatherResult.Success(weather))
//
//        // When & Then
//        useCase.getCurrentWeather(cityId).test {
//            val result = awaitItem()
//            assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
//            assertThat((result as WeatherResult.Success).data?.cityName)
//                .isEqualTo("Moscow")
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `getDailyForecast returns forecast data from repository`() = runTest {
//        // Given
//        val cityId = 1
//        val forecast = listOf(
//            DailyForecast(
//                id = 1,
//                cityId = cityId,
//                date = System.currentTimeMillis(),
//                minTemperature = 15.0,
//                maxTemperature = 25.0,
//                dayCondition = "Clear",
//                nightCondition = "Partly cloudy",
//                precipitationProbability = 10
//            )
//        )
//        every {
//            repository.getDailyForecast(cityId)
//        } returns flowOf(WeatherResult.Success(forecast))
//
//        // When & Then
//        useCase.getDailyForecast(cityId).test {
//            val result = awaitItem()
//            assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
//            assertThat((result as WeatherResult.Success).data)
//                .hasSize(1)
//            assertThat(result.data[0].dayCondition)
//                .isEqualTo("Clear")
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `getHourlyForecast returns forecast data from repository`() = runTest {
//        // Given
//        val cityId = 1
//        val forecast = listOf(
//            HourlyForecast(
//                id = 1,
//                cityId = cityId,
//                dateTime = System.currentTimeMillis(),
//                temperature = 20.0,
//                condition = "Clear",
//                precipitationProbability = 0
//            )
//        )
//        every {
//            repository.getHourlyForecast(cityId)
//        } returns flowOf(WeatherResult.Success(forecast))
//
//        // When & Then
//        useCase.getHourlyForecast(cityId).test {
//            val result = awaitItem()
//            assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
//            assertThat((result as WeatherResult.Success).data)
//                .hasSize(1)
//            assertThat(result.data[0].condition)
//                .isEqualTo("Clear")
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `getCities returns cities from repository`() = runTest {
//        // Given
//        val cities = listOf(
//            City(
//                id = 1,
//                locationKey = "123456",
//                name = "Moscow",
//                country = "Russia",
//                region = "Moscow"
//            )
//        )
//        every {
//            repository.getCities()
//        } returns flowOf(WeatherResult.Success(cities))
//
//        // When & Then
//        useCase.getCities().test {
//            val result = awaitItem()
//            assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
//            assertThat((result as WeatherResult.Success).data)
//                .hasSize(1)
//            assertThat(result.data[0].name)
//                .isEqualTo("Moscow")
//            awaitComplete()
//        }
//    }
//}