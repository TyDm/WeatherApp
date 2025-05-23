package com.tydm.weatherApp.domain.repository

import com.tydm.weatherApp.domain.model.WeatherError

interface ErrorMessageProvider {
    fun getMessage(error: WeatherError): String
} 