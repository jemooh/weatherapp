package com.jkirwa.weatherapp.data

import com.jkirwa.weatherapp.data.local.model.FavouriteWeather
import com.jkirwa.weatherapp.data.local.model.Forecast
import com.jkirwa.weatherapp.data.local.model.Weather
import com.jkirwa.weatherapp.ui.weather.viewmodel.FavouriteWeatherState
import com.jkirwa.weatherapp.ui.weather.viewmodel.WeatherForecastState
import java.util.*

internal val testWeatherResponseResult =
    WeatherForecastState(
        isSuccessRefreshingCurrentWeather = true
    )

internal val testForecastResponseResult =
    WeatherForecastState(
        isSuccessRefreshingForecast = true
    )

internal val testFetchFavouriteWeatherResult =
    FavouriteWeatherState(
        favouriteWeather = emptyList()
    )

val currentWeather =
    Weather(
        184736,
        1650209951,
        "Nairobi South",
        19,
        30,
        24,
        -1.3188,
        36.8511,
        "09n",
        "overcast clouds",
        "Clouds",
        Date()
    )

val weatherForecastList = (0..5).map {
    Forecast(
        temp = 13, icon = "14n"
    )
}

val weatherFavouriteList = (0..5).map {
    FavouriteWeather(
        184736, "3440",
        1650209951,
        "Nairobi South",
        19,
        30,
        24,
        -1.3188,
        36.8511,
        "09n",
        "overcast clouds",
        true
    )
}
