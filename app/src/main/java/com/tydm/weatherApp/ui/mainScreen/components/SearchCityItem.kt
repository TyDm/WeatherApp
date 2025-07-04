package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.domain.model.SearchItem
import com.tydm.weatherApp.ui.theme.BlueColor
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme

@Composable
fun SearchCityItem(
    searchItem: SearchItem,
    highLightText: String,
    modifier: Modifier = Modifier
){
    val fullText = "${searchItem.name}, ${searchItem.administrativeArea}, ${searchItem.country}"
    val startIndex = fullText.indexOf(highLightText, ignoreCase = true)
    val endIndex = startIndex + highLightText.length

    val annotatedString = if (startIndex >= 0 && highLightText.isNotEmpty()) {
        AnnotatedString.Builder(fullText).apply {
            addStyle(
                style = SpanStyle(color = BlueColor),
                start = startIndex,
                end = endIndex
            )
        }.toAnnotatedString()
    } else {
        AnnotatedString(fullText)
    }
    Text(
        text = annotatedString,
        style = Typography.bodyLarge,
        modifier = Modifier.padding(vertical = 12.dp).then(modifier)
    )

}

@Composable
@Preview(backgroundColor = 0xFF222524, showBackground = true)
fun SearchCityItemPreview(){
    WeatherAppTheme {
        SearchCityItem(
            searchItem = SearchItem(
                name = "Moscow",
                administrativeArea = "Moscow",
                country = "Russia",
                key = "0",
                type =  "City",
                rank = 1
            ),
            highLightText = "Mos"
        )
    }
}