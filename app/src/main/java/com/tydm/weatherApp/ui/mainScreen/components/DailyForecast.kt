package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.R
import com.tydm.weatherApp.domain.model.DailyForecast
import com.tydm.weatherApp.ui.theme.BackgroundDarkColor
import com.tydm.weatherApp.ui.theme.GreyColor
import com.tydm.weatherApp.ui.theme.GreyTextColor
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme
import com.tydm.weatherApp.ui.theme.WhiteColor
import com.tydm.weatherApp.ui.util.WeatherIconProvider
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DailyForecastColumn(
    dailyForecastList: List<DailyForecast>,
    gmtOffset: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .background(color = BackgroundDarkColor)
            .then(modifier)
    ) {
        dailyForecastList.forEachIndexed { index, item ->
            DailyForecastItem(
                dailyForecast = item,
                gmtOffset = gmtOffset
            )
            if (index != dailyForecastList.lastIndex) {
                HorizontalDivider(color = GreyColor)
            }
        }
    }
}

@Composable
private fun DailyForecastItem(
    dailyForecast: DailyForecast,
    gmtOffset: Int,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val instant = java.time.Instant.ofEpochSecond(dailyForecast.date)
    val zoneId = ZoneId.of("GMT+$gmtOffset")
    val localDateTime = instant.atZone(zoneId).toLocalDateTime()

    val dayOfWeekText = when {
        localDateTime.toLocalDate()
            .isEqual(today.plusDays(1)) -> stringResource(R.string.label_tomorrow)

        localDateTime.toLocalDate().isEqual(today) -> stringResource(R.string.label_today)
        else -> {
            val formatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())
            formatter.format(localDateTime).replaceFirstChar { it.uppercaseChar() }
        }
    }

    var monthText = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault()).format(localDateTime)

    if (Locale.getDefault().language == "ru") {
        monthText = getCorrectMonthText(monthText)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp)) {
            Text(
                text = dayOfWeekText,
                style = Typography.bodyLarge,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = monthText,
                style = Typography.bodyLarge,
                maxLines = 1,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${stringResource(R.string.label_day)}:",
                    style = Typography.bodySmall,
                    color = GreyTextColor
                )
                Text(
                    text = "${dailyForecast.temperatureMaxMetric}°",
                    style = Typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${stringResource(R.string.label_night)}:",
                    style = Typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    color = GreyTextColor
                )
                Text(
                    text = "${dailyForecast.temperatureMinMetric}°",
                    style = Typography.headlineSmall,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(WeatherIconProvider.getIconRes(dailyForecast.conditionCode)),
            contentDescription = dailyForecast.conditionText,
            modifier = modifier.size(40.dp),
            tint = WhiteColor
        )
        Spacer(modifier = Modifier.width(8.dp))
    }

}

private fun getCorrectMonthText(text: String): String {
    return text
        .lowercase()
        .replace("январь", "января")
        .replace("февраль", "февраля")
        .replace("март", "марта")
        .replace("апрель", "апреля")
        .replace("май", "мая")
        .replace("июнь", "июня")
        .replace("июль", "июля")
        .replace("август", "августа")
        .replace("сентябрь", "сентября")
        .replace("октябрь", "октября")
        .replace("ноябрь", "ноября")
        .replace("декабрь", "декабря")
}

@Preview(locale = "ru")
@Composable
fun DailyForecastColumnPreview() {
    val dailyForecast = DailyForecast(
        id = 0,
        cityId = 0,
        date = 1751539724,
        temperatureMinMetric = 23,
        temperatureMaxMetric = 28,
        temperatureMinImperial = 73,
        temperatureMaxImperial = 82,
        conditionText = "",
        conditionCode = 0,
        mobileLink = ""
    )
    WeatherAppTheme {
        DailyForecastColumn(
            dailyForecastList = listOf(
                dailyForecast,
                dailyForecast.copy(
                    id = 1,
                    date = 1751626124,
                    temperatureMinMetric = 24,
                    temperatureMaxMetric = 29
                ),
                dailyForecast.copy(id = 2, date = 1751712524, temperatureMinMetric = 22),
                dailyForecast.copy(id = 3, date = 1751798924, temperatureMaxMetric = 25),

                ),
            gmtOffset = 3
        )
    }
}