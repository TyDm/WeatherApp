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
            is WeatherError.ApiError -> when (error.code) {
                400 -> context.getString(R.string.error_api_400)
                401 -> context.getString(R.string.error_api_401)
                403 -> context.getString(R.string.error_api_403)
                404 -> context.getString(R.string.error_api_404)
                429 -> context.getString(R.string.error_api_429)
                500 -> context.getString(R.string.error_api_500)
                else -> context.getString(R.string.error_server, error.message)
            }
            is WeatherError.DatabaseError -> context.getString(R.string.error_database)
            is WeatherError.LocationError -> error.message
            is WeatherError.UnknownError -> context.getString(R.string.error_unknown)
        }
    }
} 