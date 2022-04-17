package com.jkirwa.weatherapp.ui.weather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jkirwa.weatherapp.data.local.datasource.SharedPreferences
import com.jkirwa.weatherapp.ui.weather.composable.BottomNavigationBar
import com.jkirwa.weatherapp.ui.weather.composable.Navigation
import com.jkirwa.weatherapp.ui.weather.viewmodel.WeatherViewModel
import com.jkirwa.weatherapp.utils.Util.Companion.checkIfLocationPermissionIsEnabled
import com.jkirwa.weatherapp.utils.Util.Companion.validateAndForceLocationSetting
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val sharedPreferences: SharedPreferences by inject()
    private val weatherViewModel: WeatherViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            Navigation(navController = navController)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        MainScreen()
    }

    private fun fetchRemoteWeather() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                val lat = location?.latitude.toString()
                val lon = location?.longitude.toString()
                weatherViewModel.fetchCurrentWeather(lat, lon)
                weatherViewModel.fetch5dayWeatherForecast(lat, lon)
            }
    }

    override fun onStart() {
        super.onStart()
        checkIfLocationPermissionIsEnabled(this, sharedPreferences)
        validateAndForceLocationSetting(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        fetchRemoteWeather()
    }
}
