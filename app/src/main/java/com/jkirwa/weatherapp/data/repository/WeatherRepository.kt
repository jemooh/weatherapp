package com.jkirwa.weatherapp.data.repository

import com.jkirwa.weatherapp.data.local.model.Forecast
import com.jkirwa.weatherapp.data.local.model.Weather
import com.jkirwa.weatherapp.data.remote.api.WeatherApiService
import com.jkirwa.weatherapp.data.remote.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject


interface WeatherRepository {
    suspend fun fetchCurrentLocationWeather(lat: String, lon: String)
    suspend fun fetch5dayWeatherForecast(lat: String, lon: String)
    fun getCurrentWeather(): Flow<List<Weather>>
    fun getForecast(): Flow<List<Forecast>>
    suspend fun saveFavouriteCurrentWeather(weather: Weather)
}