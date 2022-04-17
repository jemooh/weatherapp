package com.jkirwa.weatherapp.utils

import android.Manifest

object Constants {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val APIKEY = "8621094f60d974279c794e6f671ed398"
    const val UPDATE_INTERVAL: Long = 10000
    const val FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2
    const val PREFS_LATITUDE = "LATITUDE"
    const val PREFS_LONGITUDE = "LONGITUDE"
    const val PREFS_LOCATION_TIME = "LOCATION_TIME"
    const val UNITS_METRIC = "metric"
    const val DEGREE_CELSIUS_SYMBOL = " \u2103"
    const val PERMISSION_ALL = 1
    var PERMISSIONS_LOCATION = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}
