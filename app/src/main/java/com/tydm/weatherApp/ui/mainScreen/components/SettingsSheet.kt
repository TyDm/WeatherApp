package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.ui.model.CityWeatherData
import com.tydm.weatherApp.ui.theme.WeatherAppTheme

@Composable
fun SettingsSheet(
    citiesList: List<CityWeatherData>,
    onClickDelete: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    val snapFlingBehavior = rememberSnapFlingBehavior(
        lazyListState = lazyListState,
        snapPosition = SnapPosition.End
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f))
        SearchTextField(
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.weight(0.05f))
        BoxWithConstraints(modifier = Modifier.weight(0.5f)) {
            LazyColumn(
                state = lazyListState,
                flingBehavior = snapFlingBehavior,
                modifier = Modifier.fillMaxSize(),
                reverseLayout = true
            ) {
                items(
                    items = citiesList,
                    key = { cityWeatherData -> cityWeatherData.city.id }
                )
                { cityWeatherData ->

                    cityWeatherData.currentWeather?.let {
                        CityCard(
                            city = cityWeatherData.city,
                            currentWeather = cityWeatherData.currentWeather,
                            onClickDelete = { onClickDelete(cityWeatherData.city.id) },
                            modifier = Modifier
                                .height(this@BoxWithConstraints.maxHeight / 3)
                                .animateItem()
                        )
                    }

                }

            }
        }

        Spacer(modifier = Modifier.weight(0.1f))
    }
}

@Preview(backgroundColor = 0xFF222524, showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    val cityWeatherData = CityWeatherData(
        city = City(0, "", "Moscow", "", "", 3),
        currentWeather = Weather(
            id = 0,
            cityId = 0,
            observationTime = 0,
            updateTime = 0,
            temperatureMetric = 32,
            temperatureImperial = 0,
            realFeelTemperatureMetric = 28,
            realFeelTemperatureImperial = 0,
            humidity = 10,
            windMetric = 2,
            windImperial = 0f,
            conditionText = "Cloudy",
            conditionCode = 0,
            atmPrecipitation = 12,
            mobileLink = ""
        ),
        dailyForecasts = listOf(DailyForecast(0, 0, 0, 0, 0, 0, 0, "", 0, "")),
        hourlyForecasts = listOf(HourlyForecast(0, 0, 0, 0f, 0, "", 0, ""))
    )
    val cities = listOf<CityWeatherData>(
        cityWeatherData.copy(city = City(0, "", "Saint Petersburg", "", "", 3)),
        cityWeatherData.copy(city = City(1, "", "Moscow", "", "", 3)),
        cityWeatherData.copy(city = City(2, "", "Novosibirsk", "", "", 3)),
        cityWeatherData.copy(city = City(3, "", "Krasnodar", "", "", 3)),
    )

    WeatherAppTheme {
        SettingsSheet(
            citiesList = cities,
            onClickDelete = {}
        )

    }
}