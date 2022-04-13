package com.jkirwa.weatherapp.data.local.datasource

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.jkirwa.weatherapp.utils.Constants

class SharedPreferences
constructor(private val settings: SharedPreferences) {

    fun getLocation(context: Context?): Location? {
        val location = Location(
            LocationManager.GPS_PROVIDER
        )
        location.latitude = settings.getFloat(Constants.PREFS_LATITUDE, 0f).toDouble()
        location.longitude = settings.getFloat(Constants.PREFS_LONGITUDE, 0f).toDouble()
        location.time = settings.getLong(Constants.PREFS_LOCATION_TIME, 0)
        return location
    }

    fun saveLocation(
        context: Context?,
        location: Location
    ) {
        val editor = settings.edit()
        editor.putFloat(Constants.PREFS_LATITUDE, location?.latitude.toFloat())
        editor.putFloat(Constants.PREFS_LONGITUDE, location?.longitude.toFloat())
        editor.putLong(Constants.PREFS_LOCATION_TIME, location.time)
        editor.apply()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun neverAskAgainSelected(activity: Activity, permission: String?): Boolean {
        val prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission)
        val currShouldShowStatus =
            activity.shouldShowRequestPermissionRationale(permission!!)
        return prevShouldShowStatus != currShouldShowStatus
    }


    fun setShouldShowStatus(
        context: Context,
        permission: String?
    ) {
        val genPrefs = context.getSharedPreferences(
            "GENERIC_PREFERENCES",
            Context.MODE_PRIVATE
        )
        val editor = genPrefs.edit()
        editor.putBoolean(permission, true)
        editor.apply()
    }

    private fun getRatinaleDisplayStatus(
        context: Context,
        permission: String?
    ): Boolean {
        val genPrefs = context.getSharedPreferences(
            "GENERIC_PREFERENCES",
            Context.MODE_PRIVATE
        )
        return genPrefs.getBoolean(permission, false)
    }

}