package com.tydm.weatherApp.ui.mainScreen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.ui.theme.GreyText
import com.tydm.weatherApp.ui.theme.WhiteColor

@Composable
fun BottomBar(
    pagerState: PagerState
){
        Row(
            modifier = Modifier.fillMaxWidth().heightIn(60.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until pagerState.pageCount) {
                Canvas(modifier = Modifier.size(30.dp)) {
                    drawCircle(
                        color = if (i == pagerState.currentPage) WhiteColor else GreyText,
                        radius = 5.dp.toPx()
                    )
                }
            }
        }
}

@Preview
@Composable
private fun BottomBarPreview(){
    val pagerState = rememberPagerState { 3 }
    BottomBar(pagerState = pagerState)
}