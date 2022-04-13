package com.jkirwa.weatherapp.data.remote.api

import com.jkirwa.weatherapp.data.local.model.Forecast
import com.jkirwa.weatherapp.data.remote.model.ForecastResponse
import com.jkirwa.weatherapp.data.remote.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.*

interface WeatherApiService {


    @GET("weather")
    suspend fun getCurrentWeather(@Query("lat") latitude:String,
                                  @Query("lon") longitude:String
    ): Response<WeatherResponse>

    @GET("forecast")
    suspend fun fetch5dayWeatherForecast(@Query("lat") latitude:String,
                                        @Query("lon") longitude:String): Response<ForecastResponse>

}