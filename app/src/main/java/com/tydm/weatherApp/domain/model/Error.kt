package com.tydm.weatherApp.domain.model

sealed class WeatherError : Exception() {
    data class NetworkError(override val cause: Throwable) : WeatherError()
    data class ApiError(val code: Int, override val message: String) : WeatherError()
    data class DatabaseError(override val cause: Throwable) : WeatherError()
    data class LocationError(override val message: String) : WeatherError()
    data class UnknownError(override val cause: Throwable) : WeatherError()
}

sealed class WeatherResult<out T> {
    data class Success<T>(val data: T) : WeatherResult<T>()
    data class Error(val error: WeatherError) : WeatherResult<Nothing>()
    object Loading : WeatherResult<Nothing>()
} 