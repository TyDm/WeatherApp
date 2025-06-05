package com.tydm.weatherApp.ui.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tydm.weatherApp.ui.mainScreen.components.BottomBar
import com.tydm.weatherApp.ui.mainScreen.components.SearchButton
import com.tydm.weatherApp.ui.mainScreen.components.WeatherDetails
import com.tydm.weatherApp.ui.mainScreen.components.WeatherMain
import com.tydm.weatherApp.ui.theme.BackgroundDarkColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState { state.cities.size }
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .background(BackgroundDarkColor)
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            if (state.error != null) {
                ErrorMessage(
                    error = state.error!!,
                    onDismiss = { viewModel.handleIntent(MainScreenIntent.DismissError) }
                )
            } else if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(30.dp))
                    SearchButton(
                        modifier = Modifier
                            .align(Alignment.End)
                            .offset(x = (-32).dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        PullToRefreshBox(
                            isRefreshing = state.isRefreshing,
                            onRefresh = {
                                viewModel.handleIntent(
                                    MainScreenIntent.UpdateWeather(
                                        state.cities[pagerState.currentPage].city.id
                                    )) },
                            state = pullToRefreshState,
                        ){
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Bottom
                            ) { page ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 32.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.BottomCenter),
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        state.cities[page].currentWeather?.let { weather ->
                                            WeatherMain(
                                                city = state.cities[page].city,
                                                weather = weather
                                            )
                                            WeatherDetails(weather = weather)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    BottomBar(pagerState)
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    error: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            IconButton(onClick = onDismiss) {
                // Здесь должна быть иконка закрытия
            }
        }
    }
}

