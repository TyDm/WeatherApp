@file:OptIn(ExperimentalMaterial3Api::class)

package com.tydm.weatherApp.ui.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.R
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.ui.mainScreen.components.BottomBar
import com.tydm.weatherApp.ui.mainScreen.components.SearchButton
import com.tydm.weatherApp.ui.mainScreen.components.TemperatureGradientBackground
import com.tydm.weatherApp.ui.mainScreen.components.WeatherDetails
import com.tydm.weatherApp.ui.mainScreen.components.WeatherMain
import com.tydm.weatherApp.ui.model.CityWeatherData
import com.tydm.weatherApp.ui.theme.BackgroundDarkColor
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState { state.cities.size }
    val pullToRefreshState = rememberPullToRefreshState()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        LaunchedEffect(state.error) {
            if (state.error != null) {
                val result = snackBarHostState.showSnackbar(
                    message = state.error.toString(),
                    withDismissAction = true
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> viewModel.handleIntent(MainScreenIntent.DismissError)
                    SnackbarResult.Dismissed -> viewModel.handleIntent(MainScreenIntent.DismissError)
                }
            }
        }

        Box(
            modifier = Modifier
                .background(BackgroundDarkColor)
                .fillMaxSize()
        ) {
            state.cities.getOrNull(pagerState.currentPage)?.currentWeather?.let { weather ->
                TemperatureGradientBackground(
                    temperature = weather.temperatureMetric
                )
            }
            MainScreenWeather(
                modifier = Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
                state = state,
                pagerState = pagerState,
                pullToRefreshState = pullToRefreshState,
                refreshWeather = {
                    viewModel.handleIntent(
                        MainScreenIntent.UpdateWeather(
                            state.cities[pagerState.currentPage].city.id
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun MainScreenWeather(
    modifier: Modifier = Modifier,
    state: MainScreenState,
    pagerState: PagerState,
    pullToRefreshState: PullToRefreshState,
    refreshWeather: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        SearchButton(
            modifier = Modifier
                .align(Alignment.End)
                .offset(x = (-32).dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = refreshWeather,
                state = pullToRefreshState,
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) { page ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (state.cities[page].isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else if (state.cities[page].currentWeather != null &&
                            state.cities[page].hourlyForecasts.isNotEmpty() &&
                            state.cities[page].dailyForecasts.isNotEmpty()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                WeatherMain(
                                    city = state.cities[page].city,
                                    weather = state.cities[page].currentWeather!!
                                )
                                WeatherDetails(weather = state.cities[page].currentWeather!!)
                            }
                        } else {
                            Text(
                                text = stringResource(R.string.error_loading),
                                style = Typography.bodyMedium,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
        BottomBar(pagerState)
    }
}

@Preview(backgroundColor = 0xFF222524, showBackground = true, showSystemUi = true)
@Composable
private fun MainScreenWeatherPreview() {
    val cities = listOf<CityWeatherData>(
        CityWeatherData(
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
    )
    WeatherAppTheme {
        MainScreenWeather(
            state = MainScreenState(cities),
            pagerState = rememberPagerState { 1 },
            pullToRefreshState = rememberPullToRefreshState(),
            refreshWeather = {}
        )
    }
}

@Preview(backgroundColor = 0xFF222524, showBackground = true, showSystemUi = true)
@Composable
private fun MainScreenWeatherPreviewError() {
    val cities = listOf<CityWeatherData>(
        CityWeatherData(
            city = City(0, "", "Moscow", "", "", 3),
            currentWeather = null,
            dailyForecasts = listOf(),
            hourlyForecasts = listOf()
        )
    )
    WeatherAppTheme {
        MainScreenWeather(
            state = MainScreenState(cities),
            pagerState = rememberPagerState { 1 },
            pullToRefreshState = rememberPullToRefreshState(),
            refreshWeather = {}
        )
    }
}

