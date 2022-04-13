package com.jkirwa.weatherapp.data.local.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jkirwa.weatherapp.data.local.dao.ForecastDao
import com.jkirwa.weatherapp.data.local.dao.WeatherDao
import com.jkirwa.weatherapp.data.local.model.Forecast
import com.jkirwa.weatherapp.data.local.model.Weather


@Database(
    entities = [Weather::class, Forecast::class],
    version = 1

)
@TypeConverters(Converters::class)
abstract class WeatherDatabase: RoomDatabase() {
    abstract val weatherDao: WeatherDao
    abstract val forecastDao:ForecastDao

    companion object {
        const val DATABASE_NAME="weather_db"
    }
}