package com.tydm.weatherApp.ui.util

import android.content.Context
import com.tydm.weatherApp.R
import com.tydm.weatherApp.domain.model.WeatherError
import com.tydm.weatherApp.domain.repository.ErrorMessageProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidErrorMessageProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : ErrorMessageProvider {
    
    override fun getMessage(error: WeatherError): String {
        return when (error) {
            is WeatherError.NetworkError -> context.getString(R.string.error_network)
            is WeatherError.ApiError -> context.getString(R.string.error_api, error.code.toString())
            is WeatherError.DatabaseError -> context.getString(R.string.error_database)
            is WeatherError.LocationError -> error.message
            is WeatherError.UnknownError -> context.getString(R.string.error_unknown)
        }
    }
} 