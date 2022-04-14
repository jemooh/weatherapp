package com.jkirwa.weatherapp.ui.weather.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkirwa.weatherapp.data.local.model.Forecast
import com.jkirwa.weatherapp.data.local.model.Weather
import com.jkirwa.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.jkirwa.weatherapp.data.remote.model.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class WeatherViewModel(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    private val _state = mutableStateOf(WeatherForecastState())
    val state: State<WeatherForecastState> = _state

    private var getCurrentWeatherJob: Job? = null
    private var getForecastWeatherJob: Job? = null

    init {
        getCurrentWeather()
        getForecast()
    }

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
                        isErrorRefreshingCurrentWeather = true,currentWeatherErrorMessage = result.exception.message.toString()
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
                         isErrorRefreshingForecast = true,forecastErrorMessage = result.exception.message.toString()
                     )
                 }
             }
        }
    }

    private fun getCurrentWeather() {
        getCurrentWeatherJob?.cancel()
        getCurrentWeatherJob = weatherRepository.getCurrentWeather()
            .onEach { weather_ ->
                _state.value = state.value.copy(
                    weather = weather_
                )
            }.launchIn(viewModelScope)
    }

    private fun getForecast() {
        getForecastWeatherJob?.cancel()
        weatherRepository.getForecast().onEach { forecast ->
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
