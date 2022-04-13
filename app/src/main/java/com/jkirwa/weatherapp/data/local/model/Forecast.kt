package com.jkirwa.weatherapp.data.local.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["day"], unique = true)])
data class Forecast(
    @PrimaryKey
    @NonNull
    val day: String="",
    val temp: Double?,
    val icon: String?
)
