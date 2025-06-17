package com.tydm.weatherApp.ui.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.usecase.AddCityUseCase
import com.tydm.weatherApp.domain.usecase.DeleteCityUseCase
import com.tydm.weatherApp.domain.usecase.GetWeatherUseCase
import com.tydm.weatherApp.domain.usecase.UpdateWeatherUseCase
import com.tydm.weatherApp.ui.model.CityWeatherData
import com.tydm.weatherApp.ui.util.AndroidErrorMessageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addCityUseCase: AddCityUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val deleteCityUseCase: DeleteCityUseCase,
    private val updateWeatherUseCase: UpdateWeatherUseCase,
    private val errorMessageProvider: AndroidErrorMessageProvider
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state
    private var weatherJobs: MutableMap<Int, Job> = mutableMapOf()

    init {
//        addCity("294021")
        observeCities()
    }

    fun handleIntent(intent: MainScreenIntent) {
        when (intent) {
            is MainScreenIntent.AddCity -> addCity(intent.locationKey)
            is MainScreenIntent.DeleteCity -> deleteCity(intent.id)
            is MainScreenIntent.UpdateWeather -> updateWeather(intent.cityId)
            is MainScreenIntent.DismissError -> dismissError()
        }
    }

    private fun observeCities() {
        viewModelScope.launch {
            getWeatherUseCase.getCities().collect { result ->
                when (result) {
                    is WeatherResult.Success -> {
                        _state.update {
                            it.copy(
                                cities = result.data.map { city ->
                                    CityWeatherData(
                                        city = city,
                                        currentWeather = null,
                                        dailyForecasts = emptyList(),
                                        hourlyForecasts = emptyList()
                                    )
                                }
                            )
                        }

                        weatherJobs.values.forEach { it.cancel() }
                        weatherJobs.clear()

                        result.data.forEachIndexed { index, city ->
                            val job = viewModelScope.launch {
                                _state.update {
                                    val updatedCities = it.cities.toMutableList()
                                    updatedCities[index] =
                                        updatedCities[index].copy(isLoading = true)
                                    it.copy(cities = updatedCities)
                                }
                                combine(
                                    getWeatherUseCase.getCurrentWeather(city.id),
                                    getWeatherUseCase.getDailyForecast(city.id),
                                    getWeatherUseCase.getHourlyForecast(city.id)
                                ) { currentWeather, dailyForecast, hourlyForecast ->
                                    Triple(currentWeather, dailyForecast, hourlyForecast)
                                }.collect { (currentWeather, dailyForecast, hourlyForecast) ->
                                    val error = when {
                                        currentWeather is WeatherResult.Error -> currentWeather.error
                                        dailyForecast is WeatherResult.Error -> dailyForecast.error
                                        hourlyForecast is WeatherResult.Error -> hourlyForecast.error
                                        else -> null
                                    }

                                    if (error != null) {
                                        _state.update {
                                            val updatedCities = it.cities.toMutableList()
                                            updatedCities[index] =
                                                updatedCities[index].copy(isLoading = false)
                                            it.copy(
                                                error = errorMessageProvider.getMessage(error),
                                                cities = updatedCities
                                            )
                                        }
                                    } else {
                                        val loading = currentWeather is WeatherResult.Loading ||
                                                dailyForecast is WeatherResult.Loading ||
                                                hourlyForecast is WeatherResult.Loading
                                        _state.update {
                                            val updatedCities = it.cities.toMutableList()
                                            updatedCities[index] = updatedCities[index].copy(
                                                currentWeather = (currentWeather as? WeatherResult.Success)?.data,
                                                dailyForecasts = (dailyForecast as? WeatherResult.Success)?.data
                                                    ?: emptyList(),
                                                hourlyForecasts = (hourlyForecast as? WeatherResult.Success)?.data
                                                    ?: emptyList(),
                                                isLoading = loading
                                            )
                                            it.copy(
                                                cities = updatedCities
                                            )
                                        }
                                    }
                                }
                            }
                            weatherJobs[city.id] = job
                        }
                    }

                    is WeatherResult.Error -> {
                        _state.update {
                            it.copy(
                                error = errorMessageProvider.getMessage(result.error),
                                isLoading = false
                            )
                        }
                    }

                    WeatherResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }

        }
    }

    private fun updateWeather(cityId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            when (val result = updateWeatherUseCase(cityId)) {
                is WeatherResult.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }

                is WeatherResult.Error -> {
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            error = errorMessageProvider.getMessage(result.error)
                        )
                    }
                }

                WeatherResult.Loading -> {
                    _state.update { it.copy(isRefreshing = true) }
                }
            }
        }
    }

    private fun deleteCity(id: Int) {
        viewModelScope.launch {
            when (val result = deleteCityUseCase(id)) {
                is WeatherResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }

                is WeatherResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessageProvider.getMessage(result.error)
                        )
                    }
                }

                WeatherResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun addCity(locationKey: String) {
        viewModelScope.launch {
            when (val result = addCityUseCase(locationKey)) {
                is WeatherResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                }

                is WeatherResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessageProvider.getMessage(result.error)
                        )
                    }
                }

                WeatherResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}