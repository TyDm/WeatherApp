package com.tydm.weatherApp.util

import com.tydm.weatherApp.domain.model.WeatherError
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toWeatherError(): WeatherError {
    return when (this) {
        is IOException -> when (this) {
            is SocketTimeoutException -> WeatherError.NetworkError(this)
            is UnknownHostException -> WeatherError.NetworkError(this)
            else -> WeatherError.NetworkError(this)
        }
        is HttpException -> WeatherError.ApiError(
            code = code(),
            message = message() ?: "Unknown API error"
        )
        else -> WeatherError.UnknownError(this)
    }
}