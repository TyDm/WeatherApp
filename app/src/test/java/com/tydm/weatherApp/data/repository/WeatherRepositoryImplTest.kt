package com.tydm.weatherApp.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.tydm.weatherApp.data.local.dao.WeatherDao
import com.tydm.weatherApp.data.local.entity.CityEntity
import com.tydm.weatherApp.data.local.entity.WeatherEntity
import com.tydm.weatherApp.data.mapper.toDailyForecastEntityList
import com.tydm.weatherApp.data.mapper.toEntity
import com.tydm.weatherApp.data.mapper.toHourlyForecastEntityList
import com.tydm.weatherApp.data.mapper.toWeatherEntity
import com.tydm.weatherApp.data.weatherapi.AccuWeatherLanguages
import com.tydm.weatherApp.data.weatherapi.AccuweatherApi
import com.tydm.weatherApp.data.weatherapi.AdministrativeAreaInfo
import com.tydm.weatherApp.data.weatherapi.CityCountry
import com.tydm.weatherApp.data.weatherapi.CityResponse
import com.tydm.weatherApp.data.weatherapi.CurrentWeatherItem
import com.tydm.weatherApp.data.weatherapi.DailyForecastResponse
import com.tydm.weatherApp.data.weatherapi.DailyForecastsItem
import com.tydm.weatherApp.data.weatherapi.DailyTemperature
import com.tydm.weatherApp.data.weatherapi.Day
import com.tydm.weatherApp.data.weatherapi.Direction
import com.tydm.weatherApp.data.weatherapi.HourlyForecastResponseItem
import com.tydm.weatherApp.data.weatherapi.HourlyTemperature
import com.tydm.weatherApp.data.weatherapi.Imperial
import com.tydm.weatherApp.data.weatherapi.Maximum
import com.tydm.weatherApp.data.weatherapi.Metric
import com.tydm.weatherApp.data.weatherapi.Minimum
import com.tydm.weatherApp.data.weatherapi.RealFeelTemperature
import com.tydm.weatherApp.data.weatherapi.Speed
import com.tydm.weatherApp.data.weatherapi.Temperature
import com.tydm.weatherApp.data.weatherapi.TimeZone
import com.tydm.weatherApp.data.weatherapi.Wind
import com.tydm.weatherApp.domain.model.WeatherError
import com.tydm.weatherApp.domain.model.WeatherResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class WeatherRepositoryImplTest {

    private lateinit var weatherDao: WeatherDao
    private lateinit var weatherApi: AccuweatherApi
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var weatherLanguage: AccuWeatherLanguages

    @Before
    fun setup() {
        weatherDao = mockk(relaxed = true)
        weatherApi = mockk()
        weatherLanguage = mockk(relaxed = true)
        repository = WeatherRepositoryImpl(weatherDao, weatherApi, weatherLanguage)

    }

    @Test
    fun `addCity success`() = runTest {

        val locationKey = "12345678"
        val language = "en"
        val cityResponse = CityResponse(
            key = "12345678",
            type = "City",
            rank = 10,
            localizedName = "city",
            country = CityCountry("Country"),
            administrativeArea = AdministrativeAreaInfo("type", "name"),
            timeZone = TimeZone(3)
        )
        val dailyForecastResponse = DailyForecastResponse(
            listOf(
                DailyForecastsItem(
                    temperature = DailyTemperature(Minimum(10.0), Maximum(20.0)),
                    epochDate = System.currentTimeMillis(),
                    day = Day(false, "", 0, 0, "", ""),
                    date = "",
                    mobileLink = ""
                )
            )
        )
        val currentWeatherResponse = listOf(
            CurrentWeatherItem(
                temperature = Temperature(Metric(value = 0.0), Imperial(value = 0.0)),
                localObservationDateTime = "",
                epochTime = 0,
                realFeelTemperature = RealFeelTemperature(
                    Metric(value = 0.0),
                    Imperial(value = 0.0)
                ),
                relativeHumidity = 0,
                wind = Wind(Speed(Metric(value = 0.0), Imperial(value = 0.0)), Direction(0, "")),
                weatherText = (""),
                weatherIcon = 0,
                isDayTime = false,
                mobileLink = "",
                cloudCover = 0
            )
        )
        val listHourlyForecast = listOf(
            HourlyForecastResponseItem(
                temperature = HourlyTemperature(20.0),
                hasPrecipitation = false,
                epochDateTime = System.currentTimeMillis(),
                isDaylight = false,
                dateTime = "",
                iconPhrase = "",
                weatherIcon = 0,
                mobileLink = ""

            )
        )
        coEvery { weatherLanguage.language } returns language
        coEvery {
            weatherApi.getCityInfoByLocationKey(locationKey, language).body()
        } returns cityResponse
        coEvery { weatherDao.getCities() } returns flowOf(emptyList())
        coEvery { weatherDao.insertCity(cityResponse.toEntity(language, 0)) } returns 1L
        coEvery {
            weatherApi.getDailyForecast(locationKey, language).body()
        } returns dailyForecastResponse
        coEvery { weatherDao.insertDailyForecast(dailyForecastResponse.toDailyForecastEntityList(1)) } returns Unit
        coEvery {
            weatherApi.getCurrentWeather(locationKey, language).body()
        } returns currentWeatherResponse
        coEvery {
            weatherDao.insertWeather(
                currentWeatherResponse.toWeatherEntity(
                    1,
                    0
                )
            )
        } returns Unit
        coEvery {
            weatherApi.getHourlyForecast(locationKey, language).body()
        } returns listHourlyForecast
        coEvery { weatherDao.insertHourlyForecast(listHourlyForecast.toHourlyForecastEntityList(1)) } returns Unit


        val result = repository.addCity(locationKey)


        assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
    }

    @Test
    fun `addCity network error`() = runTest {

        val locationKey = "123456"
        val language = "en"

        coEvery { weatherLanguage.language } returns language
        coEvery {
            weatherApi.getCityInfoByLocationKey(locationKey, language)
        } throws IOException()


        val result = repository.addCity(locationKey)


        assertThat(result).isInstanceOf(WeatherResult.Error::class.java)
        assertThat((result as WeatherResult.Error).error)
            .isInstanceOf(WeatherError.NetworkError::class.java)
    }

    @Test
    fun `getCurrentWeather emits success state`() = runTest {

        val cityId = 1
        val weatherEntity = WeatherEntity(
            id = 1,
            cityId = cityId,
            observationTime = 0L,
            updateTime = 0L,
            temperatureMetric = 0,
            temperatureImperial = 0,
            realFeelTemperatureMetric = 0,
            realFeelTemperatureImperial = 0,
            humidity = 0,
            windMetric = 0,
            windImperial = 0f,
            conditionText = "",
            conditionCode = 0,
            atmPrecipitation = 0,
            mobileLink = ""

        )

        coEvery { weatherDao.getWeather(cityId) } returns flowOf(weatherEntity)

        repository.getCurrentWeather(cityId).test {
            val successResult = awaitItem()
            assertThat(successResult).isInstanceOf(WeatherResult.Success::class.java)
            assertThat((successResult as WeatherResult.Success).data?.cityId)
                .isEqualTo(1)
            awaitComplete()
        }
    }

    @Test
    fun `getCurrentWeather emits error state on database error`() = runTest {

        val cityId = 1
        coEvery { weatherDao.getWeather(cityId) } throws RuntimeException("Database error")

        repository.getCurrentWeather(cityId).test {
            val errorResult = awaitItem()
            assertThat(errorResult).isInstanceOf(WeatherResult.Error::class.java)
            assertThat((errorResult as WeatherResult.Error).error)
                .isInstanceOf(WeatherError.DatabaseError::class.java)
            awaitComplete()
        }
    }

    @Test
    fun `updateWeather returns api error on 404`() = runTest {
        val cityId = 1
        val language = "en"
        val cityEntity = CityEntity(
            id = cityId,
            locationKey = "123456",
            name = "Moscow",
            country = "Russia",
            administrativeArea = "Moscow",
            gmtOffset = 3,
            languageCode = "en",
            order = 0
        )

        coEvery { weatherDao.getCityById(cityId) } returns cityEntity
        coEvery { weatherLanguage.language } returns language

        val response = retrofit2.Response.error<Any>(
            404,
            "Not Found".toResponseBody(null)
        )
        coEvery {
            weatherApi.getDailyForecast(cityEntity.locationKey, language)
        } throws HttpException(response)

        val result = repository.updateWeather(cityId)

        assertThat(result).isInstanceOf(WeatherResult.Error::class.java)
        assertThat((result as WeatherResult.Error).error)
            .isInstanceOf(WeatherError.ApiError::class.java)
        assertThat((result.error as WeatherError.ApiError).code).isEqualTo(404)
    }
}