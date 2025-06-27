package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.domain.model.City
import com.tydm.weatherApp.domain.model.Weather
import com.tydm.weatherApp.ui.theme.BackgroundDarkColor
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme
import com.tydm.weatherApp.ui.theme.WhiteColor

@Composable
fun CityCard(
    city: City,
    currentWeather: Weather,
    modifier: Modifier = Modifier,
    onClickDelete: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient
                    (listOf(BackgroundDarkColor.copy(alpha = 0.3f), Color.Transparent)),
                shape = RoundedCornerShape(32.dp)
            )
            .then(modifier),
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Spacer(modifier = Modifier.weight(0.5f))
                ResizableText(
                    text = city.name,
                    maxFontSize = Typography.displayMedium.fontSize,
                    minFontSize = Typography.headlineSmall.fontSize,
                    fontStyle = Typography.displayMedium,
                    lineHeightMultiplier = 1.2f
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Text(
                    text = "${currentWeather.temperatureMetric}°",
                    style = Typography.displayLarge
                )
                Spacer(modifier = Modifier.weight(0.5f))
            }
            Column(horizontalAlignment = Alignment.End){
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onClickDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = WhiteColor)
                }
            }
        }

    }
}

@Preview(backgroundColor = 0xFFE24030, showBackground = true)
@Composable
private fun CitiesCardPreview() {
    WeatherAppTheme {
        CityCard(
            city = City(
                id = 0,
                locationKey = "",
                name = "Moscowsdsdasdsadsafsfasfsafas",
                country = "",
                administrativeArea = "",
                gmtOffset = 0
            ),
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
            modifier = Modifier.height(200.dp)
        )
    }
}