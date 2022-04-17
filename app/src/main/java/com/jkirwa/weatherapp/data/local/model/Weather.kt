package com.jkirwa.weatherapp.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value = ["locationName"], unique = true)])
data class Weather(
    @PrimaryKey
    val id: Int? = 0,
    val dt: Int?,
    val locationName: String?,
    val minTemp: Int?,
    val maxTemp: Int?,
    val temp: Int?,
    val lat: Double?,
    val lon: Double?,
    val icon: String?,
    val description: String?,
    val mainDescription: String?,
    val dateUpdated: Date,
    var isFavourite: Boolean = false
)
