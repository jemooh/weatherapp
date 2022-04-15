package com.jkirwa.weatherapp.ui.weather.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkirwa.weatherapp.data.local.model.FavouriteWeather
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

class FavouriteWeatherViewModel(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    private val _state = mutableStateOf(FavouriteWeatherState())
    val state: State<FavouriteWeatherState> = _state

    private var getForecastWeatherJob: Job? = null

    init {
        getForecast()
    }


    private fun getForecast() {
        getForecastWeatherJob?.cancel()
        getForecastWeatherJob=weatherRepository.getFavouriteWeather().onEach { weatherList ->
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
