package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.domain.model.HourlyForecast
import com.tydm.weatherApp.ui.theme.GreyTextColor
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme
import com.tydm.weatherApp.ui.theme.WhiteColor
import com.tydm.weatherApp.ui.util.WeatherIconProvider
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun HourlyForecastRow(
    hourlyForecastList: List<HourlyForecast>,
    gmtOffset: Int,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (HourlyForecast) -> Unit = {}
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        state = lazyListState,
    ) {
        item {
            Spacer(modifier = Modifier.width(16.dp))
        }
        itemsIndexed(hourlyForecastList) { index, forecast ->
            HourlyForecastItem(
                hourlyForecast = forecast,
                gmtOffset = gmtOffset,
                onClick = { onItemClick(forecast) }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
private fun HourlyForecastItem(
    hourlyForecast: HourlyForecast,
    gmtOffset: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val instant = java.time.Instant.ofEpochSecond(hourlyForecast.dateTime)
        val zoneId = ZoneId.of("GMT+$gmtOffset")
        val localDateTime = instant.atZone(zoneId).toLocalDateTime()

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        Text(
            text = timeFormatter.format(localDateTime),
            style = Typography.bodyLarge,
            color = GreyTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Icon(
            painter = painterResource(id = WeatherIconProvider.getIconRes(hourlyForecast.conditionCode)),
            contentDescription = hourlyForecast.conditionText,
            tint = WhiteColor,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = " ${hourlyForecast.temperatureMetric.roundToInt()}°",
            style = Typography.headlineSmall
        )
    }
}

@Preview(backgroundColor = 0xFF222524, showBackground = true)
@Composable
private fun HourlyForecastItemPreview() {
    WeatherAppTheme {
        HourlyForecastItem(
            hourlyForecast = HourlyForecast(
                id = 0,
                cityId = 0,
                dateTime = 1750150800,
                temperatureMetric = 27.2f,
                temperatureImperial = 0,
                conditionText = "",
                conditionCode = 0,
                mobileLink = ""
            ),
            gmtOffset = 3
        )
    }
}

@Preview(backgroundColor = 0xFF222524, showBackground = true)
@Composable
private fun HourlyForecastRowPreview() {
    WeatherAppTheme {
        HourlyForecastRow(
            lazyListState = rememberLazyListState(),
            hourlyForecastList = listOf(
                HourlyForecast(
                    id = 0,
                    cityId = 0,
                    dateTime = 1750150800,
                    temperatureMetric = 27.2f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 1,
                    cityId = 0,
                    dateTime = 1750154400,
                    temperatureMetric = 26.8f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 2,
                    cityId = 0,
                    dateTime = 1750158000,
                    temperatureMetric = 25.5f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 3,
                    cityId = 0,
                    dateTime = 1750161600,
                    temperatureMetric = 24.3f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 4,
                    cityId = 0,
                    dateTime = 1750165200,
                    temperatureMetric = 23.1f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 5,
                    cityId = 0,
                    dateTime = 1750168800,
                    temperatureMetric = 22.4f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 6,
                    cityId = 0,
                    dateTime = 1750172400,
                    temperatureMetric = 21.8f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 7,
                    cityId = 0,
                    dateTime = 1750176000,
                    temperatureMetric = 21.2f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 8,
                    cityId = 0,
                    dateTime = 1750179600,
                    temperatureMetric = 20.7f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 9,
                    cityId = 0,
                    dateTime = 1750183200,
                    temperatureMetric = 20.1f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 10,
                    cityId = 0,
                    dateTime = 1750186800,
                    temperatureMetric = 19.5f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                ),
                HourlyForecast(
                    id = 11,
                    cityId = 0,
                    dateTime = 1750190400,
                    temperatureMetric = 19.0f,
                    temperatureImperial = 0,
                    conditionText = "",
                    conditionCode = 0,
                    mobileLink = ""
                )
            ),
            gmtOffset = 3
        )
    }
}