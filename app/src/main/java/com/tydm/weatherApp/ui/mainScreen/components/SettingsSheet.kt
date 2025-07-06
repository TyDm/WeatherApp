package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.R
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.domain.model.SearchItem
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.ui.model.CityWeatherData
import com.tydm.weatherApp.ui.theme.GreyColor
import com.tydm.weatherApp.ui.theme.GreyTextColor
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme
import com.tydm.weatherApp.ui.theme.WhiteColor

@Composable
fun SettingsSheet(
    citiesList: List<CityWeatherData>,
    searchCitesList: List<SearchItem>,
    textFieldValue: TextFieldValue,
    modifier: Modifier = Modifier,
    onClickDelete: (id: Int) -> Unit = {},
    onCurrentLocationClick: (id: Int) -> Unit = {},
    onSearchItemClick: (key: String) -> Unit = {},
    onCityCardClick: (index: Int) -> Unit = {},
    isLoading: Boolean = false,
    lazyListState: LazyListState = rememberLazyListState(),
    onValueChange: (textFieldValue: TextFieldValue) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.05f))
        SearchTextField(
            textFieldValue = textFieldValue,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (isLoading) {
            true -> Box(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(top = 32.dp)
            ) {
                CircularProgressIndicator(color = GreyColor)
            }

            false -> {
                if (textFieldValue.text.isEmpty()) {
                    CityCardList(
                        citiesList = citiesList,
                        lazyListState = lazyListState,
                        onClickImHere = { onCurrentLocationClick(it) },
                        onClickDelete = onClickDelete,
                        onItemClick = { onCityCardClick(it) },
                        modifier = Modifier.weight(0.5f)
                    )
                } else {
                    SearchList(
                        searchCitesList = searchCitesList,
                        highLightText = textFieldValue.text.trim(),
                        onItemClick = { onSearchItemClick(it) },
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.1f))
    }
}

@Composable
private fun CityCardList(
    citiesList: List<CityWeatherData>,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    onClickImHere: (id: Int) -> Unit = {},
    onItemClick: (index: Int) -> Unit = {},
    onClickDelete: (id: Int) -> Unit = {}
) {
    val snapFlingBehavior = rememberSnapFlingBehavior(
        lazyListState = lazyListState,
        snapPosition = SnapPosition.End
    )
    if (citiesList.isNotEmpty()) {
        BoxWithConstraints(modifier = Modifier.then(modifier)) {
            LazyColumn(
                state = lazyListState,
                flingBehavior = snapFlingBehavior,
                modifier = Modifier.fillMaxSize(),
                reverseLayout = true
            ) {
                itemsIndexed(
                    items = citiesList,
                    key = { _, cityWeatherData -> cityWeatherData.city.id }
                )
                { index, cityWeatherData ->
                    CityCard(
                        city = cityWeatherData.city,
                        index = index,
                        currentWeather = cityWeatherData.currentWeather,
                        onClickImHere = { onClickImHere(cityWeatherData.city.id) },
                        onClickDelete = { onClickDelete(cityWeatherData.city.id) },
                        modifier = Modifier
                            .height(this@BoxWithConstraints.maxHeight / 3)
                            .animateItem()
                            .clickable { onItemClick(index) }
                    )
                }
            }
        }
    } else {
        ErrorBox(
            text = stringResource(R.string.label_cities_not_found_add),
            modifier = modifier
                .fillMaxSize()
                .imePadding(),
            color = GreyTextColor
        )
    }
}

@Composable
private fun SearchList(
    searchCitesList: List<SearchItem>,
    modifier: Modifier = Modifier,
    highLightText: String = "",
    onItemClick: (key: String) -> Unit = {}
) {
    if (searchCitesList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier),
        ) {
            itemsIndexed(
                items = searchCitesList,
                key = { _, searchItem -> searchItem.key }) { index, searchItem ->
                Column(modifier = Modifier.clickable(onClick = { onItemClick(searchItem.key) })) {
                    SearchCityItem(
                        searchItem = searchItem,
                        highLightText = highLightText,
                    )
                    if (index != searchCitesList.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
        }
    } else {
        ErrorBox(
            text = stringResource(R.string.label_cities_not_found),
            modifier = modifier
                .fillMaxSize()
                .imePadding(),
            color = WhiteColor
        )
    }
}

@Composable
private fun ErrorBox(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    Box(
        modifier = Modifier.then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(R.drawable.ic_city),
                tint = color,
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
            Text(
                text = text,
                style = Typography.bodyLarge,
                color = color
            )
        }
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
            searchCitesList = listOf(),
            textFieldValue = TextFieldValue(""),
        )

    }
}

@Preview(backgroundColor = 0xFF222524, showBackground = true)
@Composable
private fun SettingsScreenWithSearchPreview() {
    val searchItem = SearchItem(
        key = "0",
        name = "Moscow",
        administrativeArea = "Moscow",
        country = "Russia",
        rank = 5,
        type = "City"
    )
    val cities = listOf<SearchItem>(
        searchItem,
        searchItem.copy(
            key = "1",
            name = "Saint Petersburg",
            administrativeArea = "Saint Petersburg",
            rank = 4
        ),
        searchItem.copy(
            key = "2",
            name = "Novosibirsk",
            administrativeArea = "Novosibirsk",
            rank = 3
        ),
        searchItem.copy(key = "3", name = "Krasnodar", administrativeArea = "Krasnodar", rank = 2),
    )

    WeatherAppTheme {
        SearchList(
            searchCitesList = cities
        )

    }
}