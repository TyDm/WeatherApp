package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.ui.theme.GreyColor

@Composable
fun SearchButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp)
            .then(modifier)
    ) {
        Canvas(modifier = Modifier.size(60.dp)) {
            drawCircle(
                color = GreyColor,
                radius = 30.dp.toPx(),
                alpha = 0.2f
            )
        }
        Icon(
            Icons.Filled.Search,
            contentDescription = "Search",
            tint = GreyColor,
            modifier = Modifier.size(25.dp)
        )
    }
}

@Preview
@Composable
private fun SearchButtonPreview() {
    SearchButton()
}