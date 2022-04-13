package com.jkirwa.weatherapp.data.local.dao

import androidx.room.*
import com.jkirwa.weatherapp.data.local.model.Weather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao: CoroutineBaseDao<Weather> {
    @Query("SELECT * FROM Weather ")
    fun getCurrentWeather(): Flow<List<Weather>>
}