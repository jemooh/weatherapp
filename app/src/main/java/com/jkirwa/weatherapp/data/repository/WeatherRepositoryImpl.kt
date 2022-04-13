package com.jkirwa.weatherapp.data.repository

import com.jkirwa.weatherapp.data.local.dao.ForecastDao
import com.jkirwa.weatherapp.data.local.dao.WeatherDao
import com.jkirwa.weatherapp.data.local.model.Forecast
import com.jkirwa.weatherapp.data.local.model.Weather
import com.jkirwa.weatherapp.data.remote.api.WeatherApiService
import com.jkirwa.weatherapp.utils.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.util.*

internal class WeatherRepositoryImpl(
    private val weatherApiService: WeatherApiService,
    private val weatherDao: WeatherDao,
    private val forecastDao: ForecastDao,
    private val isDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {
    override suspend fun fetchCurrentLocationWeather(lat: String, lon: String) {
        try {
            val result = withContext(isDispatcher) {
                weatherApiService.getCurrentWeather(lat, lon)
            }
            if (result.isSuccessful) {
                result.body()?.apply {
                    val weather = Weather(
                        id,
                        dt,
                        name,
                        main?.tempMin,
                        main?.tempMax,
                        main?.temp,
                        coord?.lat,
                        coord?.lon,
                        weather?.get(0)?.icon,
                        weather?.get(0)?.description,
                        weather?.get(0)?.main,
                        Date()
                    )
                    weatherDao.insertAsync(weather)
                }

            } else {
                Timber.e("Error Occurred")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Timber.e(e)
        }
    }

    override suspend fun fetch5dayWeatherForecast(lat: String, lon: String) {
        try {
            val result = withContext(isDispatcher) {
                weatherApiService.fetch5dayWeatherForecast(lat, lon)
            }
            if (result.isSuccessful) {
                result.body()?.list?.forEach { listItem ->
                    val forecast = Forecast(
                        Util.getWeekDayFromUTC(listItem.dt),
                        listItem.main?.temp,
                        listItem.weather?.get(0)?.icon
                    )
                    forecastDao.insertAsync(forecast)
                }

            } else {
                Timber.e("Error Occurred")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Timber.e(e)
        }
    }

    override fun getCurrentWeather(): Flow<Weather> {
        return weatherDao.getCurrentWeather()
    }

    override fun getForecast(): Flow<List<Forecast>> {
        return forecastDao.getForecast()
    }

    override suspend fun saveFavouriteCurrentWeather(weather: Weather) {
        weatherDao.insertAsync(weather)
    }
}