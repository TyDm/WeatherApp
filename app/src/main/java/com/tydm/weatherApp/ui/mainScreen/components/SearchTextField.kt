package com.tydm.weatherApp.ui.mainScreen.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.ui.theme.BackgroundGreyColor
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme
import com.tydm.weatherApp.R

@Composable
fun SearchTextField(
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver)
    { mutableStateOf(TextFieldValue("")) }
    TextField(
        value = textFieldValue,
        onValueChange = { textFieldValue = it; onValueChange(it.text) },
        placeholder = { Text(stringResource(R.string.search), style = Typography.bodyLarge, color = BackgroundGreyColor) },
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        textStyle = Typography.headlineSmall,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BackgroundGreyColor.copy(alpha = 0.2f),
            unfocusedContainerColor = BackgroundGreyColor.copy(alpha = 0.2f),
            errorContainerColor = BackgroundGreyColor.copy(alpha = 0.2f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search",
                tint = BackgroundGreyColor,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        },
        modifier = Modifier.height(50.dp).then(modifier)

    )
}


@Preview
@Composable
private fun SearchTextFieldPreview() {
    WeatherAppTheme {
        SearchTextField(
            onValueChange = {},
            modifier = Modifier.fillMaxWidth())
    }
}