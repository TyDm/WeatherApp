@file:OptIn(ExperimentalMaterial3Api::class)

package com.tydm.weatherApp.ui.mainScreen

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.R
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.ui.mainScreen.components.BottomBar
import com.tydm.weatherApp.ui.mainScreen.components.DailyForecastColumn
import com.tydm.weatherApp.ui.mainScreen.components.HourlyForecastRow
import com.tydm.weatherApp.ui.mainScreen.components.SearchButton
import com.tydm.weatherApp.ui.mainScreen.components.SettingsSheet
import com.tydm.weatherApp.ui.mainScreen.components.TemperatureGradientBackground
import com.tydm.weatherApp.ui.mainScreen.components.WeatherDetails
import com.tydm.weatherApp.ui.mainScreen.components.WeatherMain
import com.tydm.weatherApp.ui.model.CityWeatherData
import com.tydm.weatherApp.ui.theme.BackgroundDarkColor
import com.tydm.weatherApp.ui.theme.GreyColor
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val viewModelState by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState()

    val pagerState = rememberPagerState { viewModelState.cities.size }
    val pullToRefreshState = rememberPullToRefreshState()
    val snackBarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

    val keyboardController = LocalSoftwareKeyboardController.current
    var searchTextFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver)
    { mutableStateOf(TextFieldValue("")) }
    val maxSheetOffset = LocalWindowInfo.current.containerSize.height.toFloat()
    val sheetSwipeEnabled = remember {
        derivedStateOf {
            viewModelState.cities.isNotEmpty()
        }
    }
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        snackbarHost = {
            SnackbarHost(
                snackBarHostState,
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        },
        scaffoldState = sheetScaffoldState,
        sheetContainerColor = Color.Transparent,
        sheetDragHandle = null,
        sheetShadowElevation = 0.dp,
        sheetSwipeEnabled = sheetSwipeEnabled.value,
        sheetContent = {
            SettingsSheet(
                citiesList = viewModelState.cities,
                searchCitesList = viewModelState.searchCitiesList,
                textFieldValue = searchTextFieldValue,
                isLoading = viewModelState.isLoading,
                onCurrentLocationClick = { viewModel.handleIntent(MainScreenIntent.MoveCityToTop(it)) },
                onClickDelete = { viewModel.handleIntent(MainScreenIntent.DeleteCity(id = it)) },
                onSearchItemClick = {
                    viewModel.handleIntent(MainScreenIntent.AddCity(locationKey = it))
                    scope.launch {
                        keyboardController?.hide()
                        bottomSheetState.hide()
                    }
                },
                onCityCardClick = {
                    scope.launch {
                        pagerState.scrollToPage(it)
                        bottomSheetState.hide()
                    }
                },
                onValueChange = {
                    searchTextFieldValue = it
                    viewModel.handleIntent(MainScreenIntent.SearchCity(cityName = it.text))
                }
            )
        }
    ) { paddingValues ->

        LaunchedEffect(effect) {
            val e = effect ?: return@LaunchedEffect
            when (e) {
                is MainScreenEffect.ShowError -> {
                    snackBarHostState.showSnackbar(
                        message = e.message,
                        withDismissAction = true
                    )
                    viewModel.handleIntent(MainScreenIntent.ClearEffect)
                }

                is MainScreenEffect.ScrollToCity -> {
                    val index = viewModelState.cities.indexOfFirst { it.city.id == e.cityId }
                    if (index != -1) {
                        pagerState.animateScrollToPage(index)
                    }
                    viewModel.handleIntent(MainScreenIntent.ClearEffect)
                }

                is MainScreenEffect.ShowBottomSheet -> {
                    bottomSheetState.show()
                    viewModel.handleIntent(MainScreenIntent.ClearEffect)
                }
            }
        }

        LaunchedEffect(bottomSheetState.currentValue) {
            if (bottomSheetState.currentValue == SheetValue.Hidden) {
                searchTextFieldValue = TextFieldValue("")
            }
        }

        BackHandler(enabled = searchTextFieldValue.text.isNotEmpty()) {
            searchTextFieldValue = TextFieldValue("")
        }

        BackHandler(enabled = searchTextFieldValue.text.isEmpty() && bottomSheetState.isVisible) {
            scope.launch { bottomSheetState.hide() }
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
                    viewModelState.cities.getOrNull(pagerState.currentPage)?.let { cityData ->
                        viewModel.handleIntent(
                            MainScreenIntent.UpdateWeather(cityData.city.id)
                        )
                    }
                },
                openSettings = { scope.launch { bottomSheetState.show(); } },
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding()
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
                WeatherPage(
                    state = state,
                    page = page,
                    pagerState = pagerState,
                    pullToRefreshState = pullToRefreshState,
                    refreshWeather = refreshWeather
                )
            }
        }
        BottomBar(pagerState)
    }
}

@Composable
private fun WeatherPage(
    state: MainScreenState,
    page: Int,
    pagerState: PagerState,
    pullToRefreshState: PullToRefreshState,
    refreshWeather: () -> Unit
) {
    val columnLazyListState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(
        lazyListState = columnLazyListState,
        snapPosition = SnapPosition.End
    )

    if (state.cities.isEmpty() || page >= state.cities.size) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = GreyColor)
        }
        return
    }

    if (state.cities[page].currentWeather == null) {
        when (state.isLoading) {
            true -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreyColor)
                }
            }

            false -> {
                ErrorScreen(
                    state = state,
                    refreshWeather = refreshWeather,
                    pullToRefreshState = pullToRefreshState
                )
            }
        }
        return
    }

    state.cities[page].currentWeather?.let { weather ->
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
                                    weather = weather,
                                    modifier = Modifier
                                        .padding(horizontal = 32.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                        item {
                            HourlyForecastRow(
                                hourlyForecastList = state.cities[page].hourlyForecasts,
                                gmtOffset = state.cities[page].city.gmtOffset
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            DailyForecastColumn(
                                dailyForecastList = state.cities[page].dailyForecasts,
                                gmtOffset = state.cities[page].city.gmtOffset,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
            WeatherDetails(
                weather = weather,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
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

private fun SheetState.currentOffset() = try {
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

