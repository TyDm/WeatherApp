package com.tydm.weatherApp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.tydm.weatherApp.ui.mainScreen.MainScreen
import com.tydm.weatherApp.ui.mainScreen.MainViewModel
import com.tydm.weatherApp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val mainViewModel: MainViewModel by viewModels()
        setContent {
            WeatherAppTheme {
                MainScreen(mainViewModel)
            }
        }
    }
} 