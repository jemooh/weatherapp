package com.jkirwa.weatherapp.ui.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkirwa.weatherapp.data.local.model.FavouriteWeather
import com.jkirwa.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavouriteWeatherViewModel(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(FavouriteWeatherState())
    val state: StateFlow<FavouriteWeatherState> = _state

    private var getForecastWeatherJob: Job? = null

    fun getFavouriteWeather() {
        getForecastWeatherJob?.cancel()
        getForecastWeatherJob = weatherRepository.getFavouriteWeather().onEach { weatherList ->
            _state.value = state.value.copy(
                favouriteWeather = weatherList
            )
        }.launchIn(viewModelScope)
    }

    fun saveFavouriteWeather(weather: FavouriteWeather) {
        viewModelScope.launch {
            weatherRepository.saveFavouriteCurrentWeather(weather = weather)
        }
    }
}

data class FavouriteWeatherState(
    val favouriteWeather: List<FavouriteWeather> = emptyList()
)
