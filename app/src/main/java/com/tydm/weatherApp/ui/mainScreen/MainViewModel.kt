@file:OptIn(ExperimentalCoroutinesApi::class)

package com.tydm.weatherApp.ui.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.usecase.AddCityUseCase
import com.tydm.weatherApp.domain.usecase.DeleteCityUseCase
import com.tydm.weatherApp.domain.usecase.GetWeatherUseCase
import com.tydm.weatherApp.domain.usecase.SearchCityUseCase
import com.tydm.weatherApp.domain.usecase.UpdateWeatherUseCase
import com.tydm.weatherApp.ui.model.CityWeatherData
import com.tydm.weatherApp.ui.util.AndroidErrorMessageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private val errorMessageProvider: AndroidErrorMessageProvider,
    private val searchCityUseCase: SearchCityUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    private val _effect = MutableStateFlow<MainScreenEffect?>(null)
    val effect: StateFlow<MainScreenEffect?> = _effect

    private var job: Job? = null

    init {
        observeCities()
    }

    fun handleIntent(intent: MainScreenIntent) {
        when (intent) {
            is MainScreenIntent.AddCity -> addCity(intent.locationKey)
            is MainScreenIntent.DeleteCity -> deleteCity(intent.id)
            is MainScreenIntent.UpdateWeather -> updateWeather(intent.cityId)
            is MainScreenIntent.ClearEffect -> clearEffect()
            is MainScreenIntent.SearchCity -> searchCity(intent.cityName)
            is MainScreenIntent.MoveCityToTop -> moveCityToTop(intent.cityId)
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
                            _effect.value = MainScreenEffect
                                .ShowError(errorMessageProvider.getMessage(result.error))
                            flowOf(emptyList<CityWeatherData>())
                        }
                    }
                }
                .collect { cityWeatherDataList ->
                    _state.update {
                        it.copy(
                            cities = cityWeatherDataList,
                        )
                    }
                    if (cityWeatherDataList.isEmpty()) {
                        _effect.value = MainScreenEffect.ShowBottomSheet
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
                    Log.e("MainViewModel", "Ошибка при обновлении погоды: ${result.error}")
                    _effect.value = MainScreenEffect
                        .ShowError(errorMessageProvider.getMessage(result.error))
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                        )
                    }
                }
            }
        }
    }

    private fun deleteCity(id: Int) {
        viewModelScope.launch {
            when (val result = deleteCityUseCase(id)) {
                is WeatherResult.Success -> {}

                is WeatherResult.Error -> {
                    Log.e("MainViewModel", "Ошибка при удалении города: ${result.error}")
                    _effect.value = MainScreenEffect
                        .ShowError(errorMessageProvider.getMessage(result.error))
                }
            }
        }
    }

    private fun addCity(locationKey: String) {
        val existingCity = state.value.cities.find { it.city.locationKey == locationKey }
        if (existingCity != null) {
            _effect.value = MainScreenEffect.ScrollToCity(existingCity.city.id)
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = addCityUseCase(locationKey)) {
                is WeatherResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.value = MainScreenEffect.ScrollToCity(result.data)
                }

                is WeatherResult.Error -> {
                    Log.e("MainViewModel", "Ошибка при добавлении города: ${result.error}")
                    _state.update { it.copy(isLoading = false) }
                    _effect.value = MainScreenEffect
                        .ShowError(errorMessageProvider.getMessage(result.error))
                }
            }
        }
    }

    private fun searchCity(query: String) {
        if (query.isEmpty()) {
            _state.update { it.copy(isLoading = false) }
            job?.cancel()
            return
        }
        _state.update { it.copy(isLoading = true) }
        job?.cancel()
        job = viewModelScope.launch {
            delay(500)
            when (val result = searchCityUseCase(query.trim())) {
                is WeatherResult.Success -> {
                    val sortedList = result.data.sortedBy { it.rank }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            searchCitiesList = sortedList
                        )
                    }
                }

                is WeatherResult.Error -> {
                    if (result.error.cause is kotlinx.coroutines.CancellationException) return@launch
                    Log.e("MainViewModel", "Ошибка поиска города: ${result.error}")
                    _effect.value = MainScreenEffect
                        .ShowError(errorMessageProvider.getMessage(result.error))
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun clearEffect() {
        _effect.value = null
    }

    private fun moveCityToTop(cityId: Int) {
        viewModelScope.launch {
            getWeatherUseCase.moveCityToTop(cityId)
        }
    }
}