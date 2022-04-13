package com.jkirwa.weatherapp.ui.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkirwa.weatherapp.data.local.model.Forecast
import com.jkirwa.weatherapp.data.local.model.Weather
import com.jkirwa.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    fun fetchCurrentWeather(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.fetchCurrentLocationWeather(lat, lon)
        }
    }

    fun fetch5dayWeatherForecast(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.fetch5dayWeatherForecast(lat, lon)
        }
    }

    fun getCurrentWeather(): Flow<Weather> {
        return weatherRepository.getCurrentWeather()
    }

    fun getForecast(): Flow<List<Forecast>> {
        return weatherRepository.getForecast()
    }

}