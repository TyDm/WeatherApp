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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.R
import com.tydm.weatherApp.ui.theme.GreyColor
import com.tydm.weatherApp.ui.theme.Typography
import com.tydm.weatherApp.ui.theme.WeatherAppTheme

@Composable
fun SearchTextField(
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    textFieldValue: TextFieldValue
) {
    TextField(
        value = textFieldValue,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(stringResource(R.string.label_search), style = Typography.bodyLarge, color = GreyColor) },
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        textStyle = Typography.bodyLarge,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = GreyColor.copy(alpha = 0.2f),
            unfocusedContainerColor = GreyColor.copy(alpha = 0.2f),
            errorContainerColor = GreyColor.copy(alpha = 0.2f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search",
                tint = GreyColor,
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
            textFieldValue = TextFieldValue(),
            modifier = Modifier.fillMaxWidth())
    }
}