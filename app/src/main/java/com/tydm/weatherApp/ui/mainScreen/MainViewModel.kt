package com.tydm.weatherApp.ui.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.usecase.AddCityUseCase
import com.tydm.weatherApp.domain.usecase.DeleteCityUseCase
import com.tydm.weatherApp.domain.usecase.GetWeatherUseCase
import com.tydm.weatherApp.domain.usecase.UpdateWeatherUseCase
import com.tydm.weatherApp.ui.mapper.toUiModel
import com.tydm.weatherApp.ui.util.AndroidErrorMessageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    init {
        observeCities()
    }

    fun handleIntent(intent: MainScreenIntent) {
        when (intent) {
            is MainScreenIntent.AddCity -> addCity(intent.locationKey)
            is MainScreenIntent.DeleteCity -> deleteCity(intent.id)
            is MainScreenIntent.SelectCity -> observeWeather(intent.id)
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
                                cities = result.data.map { city -> city.toUiModel() },
                                isLoading = false
                            )
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
            when (val result = updateWeatherUseCase(cityId)) {
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

    private fun observeWeather(id: Int) {
        viewModelScope.launch {
            getWeatherUseCase.getCurrentWeather(id).collect { result ->
                when (result) {
                    is WeatherResult.Success -> {
                        _state.update {
                            var cities = it.cities
                            cities[0].copy(currentWeather = result.data)
                            it.copy(
                                cities = it.cities[5]
                                currentWeathers = result.data?.toUiModel(),
                                isLoading = false,
                            )
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

            getWeatherUseCase.getDailyForecast(id).collect { result ->
                when (result) {
                    is WeatherResult.Success -> {
                        _state.update {
                            it.copy(
                                dailyForecast = result.data.map { it.toUiModel() },
                                isLoading = false
                            )
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

            getWeatherUseCase.getHourlyForecast(id).collect { result ->
                when (result) {
                    is WeatherResult.Success -> {
                        _state.update {
                            it.copy(
                                hourlyForecast = result.data.map { it.toUiModel() },
                                isLoading = false
                            )
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

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}