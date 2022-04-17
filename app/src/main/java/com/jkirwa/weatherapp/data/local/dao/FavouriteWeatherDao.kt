package com.jkirwa.weatherapp.data.local.dao

import androidx.room.*
import com.jkirwa.weatherapp.data.local.model.FavouriteWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteWeatherDao : CoroutineBaseDao<FavouriteWeather> {
    @Query("SELECT * FROM FavouriteWeather ")
    fun getFavouriteWeather(): Flow<List<FavouriteWeather>>
}
