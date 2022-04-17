package com.jkirwa.weatherapp.ui.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkirwa.weatherapp.data.local.model.Forecast
import com.jkirwa.weatherapp.data.local.model.Weather
import com.jkirwa.weatherapp.data.remote.model.Result
import com.jkirwa.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(WeatherForecastState())
    val state: StateFlow<WeatherForecastState> = _state

    private var getCurrentWeatherJob: Job? = null
    private var getForecastWeatherJob: Job? = null


    fun fetchCurrentWeather(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = weatherRepository.fetchCurrentLocationWeather(lat, lon)) {
                is Result.Loading -> {
                    _state.value = state.value.copy(
                        isRefreshingCurrentWeather = true
                    )
                }
                is Result.Success -> {
                    _state.value = state.value.copy(
                        isSuccessRefreshingCurrentWeather = true
                    )
                }
                is Result.Error -> {
                    _state.value = state.value.copy(
                        isErrorRefreshingCurrentWeather = true,
                        currentWeatherErrorMessage = result.exception.message.toString()
                    )
                }
            }
        }
    }

    fun fetch5dayWeatherForecast(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.fetch5dayWeatherForecast(lat, lon)
            when (val result = weatherRepository.fetch5dayWeatherForecast(lat, lon)) {
                is Result.Loading -> {
                    _state.value = state.value.copy(
                        isRefreshingForecast = true
                    )
                }
                is Result.Success -> {
                    _state.value = state.value.copy(
                        isSuccessRefreshingForecast = true
                    )
                }
                is Result.Error -> {
                    _state.value = state.value.copy(
                        isErrorRefreshingForecast = true,
                        forecastErrorMessage = result.exception.message.toString()
                    )
                }
            }
        }
    }

    fun getCurrentWeather() {
        getCurrentWeatherJob?.cancel()
        getCurrentWeatherJob = weatherRepository.getCurrentWeather()
            .onEach { weather_ ->
                _state.value = state.value.copy(
                    weather = weather_
                )
            }.launchIn(viewModelScope)
    }

    fun getForecast() {
        getForecastWeatherJob?.cancel()
        getForecastWeatherJob = weatherRepository.getForecast().onEach { forecast ->
            _state.value = state.value.copy(
                forecast = forecast
            )
        }.launchIn(viewModelScope)
    }
}

data class WeatherForecastState(
    val forecast: List<Forecast> = emptyList(),
    val weather: Weather? = null,
    val isSuccessRefreshingCurrentWeather: Boolean = false,
    val isSuccessRefreshingForecast: Boolean = false,
    val isRefreshingCurrentWeather: Boolean = false,
    val isRefreshingForecast: Boolean = false,
    val isErrorRefreshingCurrentWeather: Boolean = false,
    val currentWeatherErrorMessage: String = "",
    val forecastErrorMessage: String = "",
    val isErrorRefreshingForecast: Boolean = false
)
