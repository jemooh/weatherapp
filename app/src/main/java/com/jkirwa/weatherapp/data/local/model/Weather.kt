package com.jkirwa.weatherapp.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jkirwa.weatherapp.data.remote.model.WeatherItem
import java.util.*

@Entity(indices = [Index(value = ["id"], unique = true)])
data class Weather(
    @PrimaryKey
    val id: Int? = 0,
    val dt: Int?,
    val locationName: String?,
    val minTemp: Double?,
    val maxTemp: Double?,
    val temp: Double?,
    val lat: Double?,
    val lon: Double?,
    val icon: String?,
    val description: String?,
    val mainDescription: String?,
    val dateUpdated: Date
)
