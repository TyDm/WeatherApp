@file:OptIn(ExperimentalCoroutinesApi::class)

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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    private val _isListWasLoaded = MutableStateFlow(false)
    val isListWasLoaded: StateFlow<Boolean> = _isListWasLoaded

    init {
//        addCity("295146")
//        addCity("294459")
//        addCity("289484")
//        addCity("294021")
        observeCities()
    }
    //INSERT INTO cities
    //SELECT * FROM cities_backup
//    INSERT INTO current_weather
//    SELECT * FROM current_Weather_backup

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
            getWeatherUseCase.getCities()
                .flatMapLatest { result ->
                    when (result) {
                        is WeatherResult.Success -> {
                            val cities = result.data
                            if (result.data.isEmpty())
                                return@flatMapLatest flowOf(emptyList<CityWeatherData>())
                            val cityWeatherFlows = cities.map { city ->
                                combine(
                                    getWeatherUseCase.getCurrentWeather(city.id),
                                    getWeatherUseCase.getDailyForecast(city.id),
                                    getWeatherUseCase.getHourlyForecast(city.id)
                                ) { currentWeather, dailyForecast, hourlyForecast ->
                                    CityWeatherData(
                                        city = city,
                                        currentWeather = (currentWeather as? WeatherResult.Success)?.data,
                                        dailyForecasts = (dailyForecast as? WeatherResult.Success)?.data
                                            ?: emptyList(),
                                        hourlyForecasts = (hourlyForecast as? WeatherResult.Success)?.data
                                            ?: emptyList(),
                                        isLoading = currentWeather !is WeatherResult.Success ||
                                                dailyForecast !is WeatherResult.Success ||
                                                hourlyForecast !is WeatherResult.Success
                                    )
                                }
                            }
                            combine(cityWeatherFlows) { it.toList() }
                        }

                        is WeatherResult.Error -> {
                            _state.update {
                                it.copy(
                                    error = errorMessageProvider.getMessage(result.error),
                                    isLoading = false
                                )
                            }
                            flowOf(emptyList<CityWeatherData>())
                        }
                    }
                }
                .collect { cityWeatherDataList ->
                    _state.update {
                        it.copy(
                            cities = cityWeatherDataList
                        )
                    }
                    _isListWasLoaded.update { true }
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
            }
        }
    }


    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}