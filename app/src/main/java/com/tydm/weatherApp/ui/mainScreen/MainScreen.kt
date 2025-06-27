@file:OptIn(ExperimentalMaterial3Api::class)

package com.tydm.weatherApp.ui.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.R
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.ui.mainScreen.components.BottomBar
import com.tydm.weatherApp.ui.mainScreen.components.HourlyForecastRow
import com.tydm.weatherApp.ui.mainScreen.components.SearchButton
import com.tydm.weatherApp.ui.mainScreen.components.SettingsSheet
import com.tydm.weatherApp.ui.mainScreen.components.TemperatureGradientBackground
import com.tydm.weatherApp.ui.mainScreen.components.WeatherDetails
import com.tydm.weatherApp.ui.mainScreen.components.WeatherMain
import com.tydm.weatherApp.ui.model.CityWeatherData
import com.tydm.weatherApp.ui.theme.BackgroundDarkColor
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val viewModelState by viewModel.state.collectAsState()
    val isListWasLoaded by viewModel.isListWasLoaded.collectAsState()
    val pagerState = rememberPagerState { viewModelState.cities.size }
    val pullToRefreshState = rememberPullToRefreshState()
    val snackBarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)
    val maxSheetOffset = LocalWindowInfo.current.containerSize.height.toFloat()
    val sheetSwipeEnabled = remember {
        derivedStateOf {
            viewModelState.cities.isNotEmpty()
        }
    }
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        scaffoldState = sheetScaffoldState,
        sheetContainerColor = Color.Transparent,
        sheetDragHandle = null,
        sheetShadowElevation = 0.dp,
        sheetSwipeEnabled = sheetSwipeEnabled.value,
        sheetContent = {
            SettingsSheet(
                citiesList = viewModelState.cities,
                onClickDelete = { viewModel.handleIntent(MainScreenIntent.DeleteCity(it)) }
            )
        }
    ) { paddingValues ->

        LaunchedEffect(viewModelState.error) {
            if (viewModelState.error != null) {
                val result = snackBarHostState.showSnackbar(
                    message = viewModelState.error.toString(),
                    withDismissAction = true
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> viewModel.handleIntent(MainScreenIntent.DismissError)
                    SnackbarResult.Dismissed -> viewModel.handleIntent(MainScreenIntent.DismissError)
                }
            }
        }

        LaunchedEffect(isListWasLoaded, viewModelState.cities) {
            if (isListWasLoaded && viewModelState.cities.isEmpty()){
                bottomSheetState.show()
            }
        }

        Box(
            modifier = Modifier
                .background(BackgroundDarkColor)
                .fillMaxSize()
        ) {
            viewModelState.cities.getOrNull(pagerState.currentPage)?.currentWeather?.let { weather ->
                TemperatureGradientBackground(
                    temperature = weather.temperatureMetric
                )
            }
            MainScreenWeather(
                state = viewModelState,
                pagerState = pagerState,
                pullToRefreshState = pullToRefreshState,
                refreshWeather = {
                    viewModel.handleIntent(
                        MainScreenIntent.UpdateWeather(
                            viewModelState.cities[pagerState.currentPage].city.id
                        )
                    )
                },
                openSettings = { scope.launch { bottomSheetState.show(); } },
                modifier = Modifier
                    .statusBarsPadding()
                    .graphicsLayer(
                        alpha =
                            (bottomSheetState.currentOffset() / maxSheetOffset).coerceIn(0f, 1f)
                    )
            )
        }
    }
}

@Composable
private fun MainScreenWeather(
    state: MainScreenState,
    pagerState: PagerState,
    pullToRefreshState: PullToRefreshState,
    modifier: Modifier = Modifier,
    refreshWeather: () -> Unit,
    openSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        SearchButton(
            modifier = Modifier
                .align(Alignment.End)
                .offset(x = (-32).dp),
            onClick = openSettings
        )
        Column(modifier = Modifier.weight(1f)) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) { page ->
                val columnLazyListState = rememberLazyListState()
                val snapFlingBehavior = rememberSnapFlingBehavior(
                    lazyListState = columnLazyListState,
                    snapPosition = SnapPosition.End
                )
                if (state.cities[page].isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                    }
                } else if (state.cities[page].currentWeather != null
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            val boxHeight = remember() { mutableStateOf(this.maxHeight) }

                            LaunchedEffect(pagerState.currentPage) {
                                columnLazyListState.animateScrollToItem(0)
                            }

                            PullToRefreshBox(
                                isRefreshing = state.isRefreshing,
                                onRefresh = refreshWeather,
                                state = pullToRefreshState,
                                modifier = Modifier.align(Alignment.BottomCenter)
                            ) {
                                LazyColumn(
                                    state = columnLazyListState,
                                    flingBehavior = snapFlingBehavior,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    item {
                                        Column(modifier = Modifier.height(boxHeight.value)) {
                                            Spacer(modifier = Modifier.weight(1f))
                                            WeatherMain(
                                                city = state.cities[page].city,
                                                weather = state.cities[page].currentWeather!!,
                                                modifier = Modifier
                                                    .padding(horizontal = 32.dp)
                                            )
                                            Spacer(modifier = Modifier.height(32.dp))
                                        }
                                    }
                                    item {
                                        HourlyForecastRow(
                                            hourlyForecastList = state.cities[page].hourlyForecasts,
                                            gmtOffset = state.cities[page].city.gmtOffset
                                        )
                                        Spacer(modifier = Modifier.height(32.dp))
                                    }
                                }
                            }
                        }
                        WeatherDetails(
                            weather = state.cities[page].currentWeather!!,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                } else {
                    ErrorScreen(
                        state = state,
                        refreshWeather = refreshWeather,
                        pullToRefreshState = pullToRefreshState
                    )
                }
            }

        }
        BottomBar(pagerState)
    }
}

@Composable
private fun ErrorScreen(
    state: MainScreenState,
    refreshWeather: () -> Unit,
    pullToRefreshState: PullToRefreshState,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = refreshWeather,
        state = pullToRefreshState,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .then(modifier)
        ) {
            Text(
                text = stringResource(R.string.error_loading),
                style = Typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

fun SheetState.currentOffset() = try {
    this.requireOffset()
} catch (_: IllegalStateException) {
    0f
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
            refreshWeather = {},
            openSettings = {}
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
            refreshWeather = {},
            openSettings = {}
        )
    }
}

