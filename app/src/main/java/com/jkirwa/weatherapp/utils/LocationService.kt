package com.jkirwa.weatherapp.utils

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.jkirwa.weatherapp.data.local.datasource.SharedPreferences
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * LocationService.
 */
class LocationService(private val context: Context) : Service() {
    private var locationListener: LocationListener? =
        null
    private var location: Location? = null
    private val sharedPreferences: SharedPreferences by inject()
    private val binder: IBinder = LocationServiceBinder()
    override fun onBind(intent: Intent): IBinder? {
        return binder
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocationServiceBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        val service: LocationService
            get() =// Return this instance of LocalService so clients can call public methods
                this@LocationService
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = Constants.UPDATE_INTERVAL
        locationRequest.fastestInterval = Constants.FASTEST_UPDATE_INTERVAL
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()
        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(locationSettingsRequest)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(
            locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) { // do work here
                    onLocationChanged(locationResult.lastLocation)
                }
            },
            Looper.myLooper()
        )
    }

    private fun onLocationChanged(location: Location) {
        this.location = location
        sharedPreferences.saveLocation(context, this.location!!)
        if (locationListener != null) {
            locationListener!!.onLocationChanged(location)
        }
    }

    fun setLocationListener(locationListener: LocationListener?) {
        this.locationListener = locationListener
    }

    /**
     * interface LocationService.
     */
    interface LocationListener {
        fun onLocationChanged(location: Location?)
    }

    companion object {
        private const val TAG = "LocationService"
    }

    init {
        startLocationUpdates()
    }
}