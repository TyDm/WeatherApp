package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.R
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.ui.theme.GreyText
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme


@Composable
fun WeatherMain(
    city: City,
    weather: Weather
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = city.name, style = Typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = weather.conditionText, style = Typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${stringResource(R.string.feels_like)} ${weather.realFeelTemperatureMetric}°",
            style = Typography.bodySmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "${weather.temperatureMetric}°", style = Typography.headlineLarge)
    }
}

@Composable
fun WeatherDetails(
    weather: Weather
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.wind),
                style = Typography.bodySmall,
                color = GreyText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "${weather.windMetric} ${stringResource(R.string.speed_ms)}",
                style = Typography.bodyMedium
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.humidity),
                style = Typography.bodySmall,
                color = GreyText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "${weather.humidity} %",
                style = Typography.bodyMedium
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.precipitation),
                style = Typography.bodySmall,
                color = GreyText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "${weather.atmPrecipitation} %",
                style = Typography.bodyMedium
            )
        }
    }
}

@Preview(backgroundColor = 0xFF222524, showBackground = true)
@Composable
private fun WeatherMainPreview() {
    WeatherAppTheme {
        WeatherMain(
            City(
                id = 0,
                locationKey = "",
                name = "Moscow",
                country = "",
                administrativeArea = "",
                gmtOffset = 0
            ),
            Weather(
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
            )
        )
    }
}

@Preview(backgroundColor = 0xFF222524, showBackground = true)
@Composable
private fun WeatherDetailsPreview() {
    WeatherAppTheme {
        WeatherDetails(
            Weather(
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
            )
        )
    }
}