package com.jkirwa.weatherapp.data.repository

import com.jkirwa.weatherapp.data.local.model.FavouriteWeather
import com.jkirwa.weatherapp.data.local.model.Forecast
import com.jkirwa.weatherapp.data.local.model.Weather
import com.jkirwa.weatherapp.data.remote.model.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun fetchCurrentLocationWeather(lat: String, lon: String): Result<Boolean>
    suspend fun fetch5dayWeatherForecast(lat: String, lon: String): Result<Boolean>
    fun getCurrentWeather(): Flow<Weather>
    fun getForecast(): Flow<List<Forecast>>
    fun getFavouriteWeather(): Flow<List<FavouriteWeather>>
    suspend fun saveFavouriteCurrentWeather(weather: FavouriteWeather)
}
