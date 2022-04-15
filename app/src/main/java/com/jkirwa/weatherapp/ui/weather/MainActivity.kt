package com.jkirwa.weatherapp.ui.weather

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.jkirwa.weatherapp.R
import com.jkirwa.weatherapp.data.local.datasource.SharedPreferences
import com.jkirwa.weatherapp.ui.weather.viewmodel.WeatherViewModel
import com.jkirwa.weatherapp.utils.Constants
import com.jkirwa.weatherapp.utils.Constants.PERMISSION_ALL
import com.jkirwa.weatherapp.utils.LocationService
import com.jkirwa.weatherapp.utils.Util
import com.jkirwa.weatherapp.utils.Util.Companion.hasPermissions
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jkirwa.weatherapp.ui.weather.composable.BottomNavigationBar
import com.jkirwa.weatherapp.ui.weather.composable.CurrentLocationWeather
import com.jkirwa.weatherapp.ui.weather.composable.FavouriteLocationWeather
import com.jkirwa.weatherapp.ui.weather.composable.Navigation
import com.jkirwa.weatherapp.ui.weather.theme.TopBar
import com.jkirwa.weatherapp.ui.weather.theme.WeatherAppTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val sharedPreferences: SharedPreferences by inject()
    private val weatherViewModel: WeatherViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
        fetchRemoteWeather()
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
        val lat = sharedPreferences.getLocation(this)?.latitude.toString()
        val lon = sharedPreferences.getLocation(this)?.longitude.toString()
        weatherViewModel.fetchCurrentWeather(lat, lon)
        weatherViewModel.fetch5dayWeatherForecast(lat, lon)
    }

    override fun onStart() {
        super.onStart()
        checkLocationPermission()
        Util.validateAndForceLocationSetting(this)
        LocationService(this)
    }

    override fun onStop() {
        super.onStop()
        stopService(Intent(this, LocationService::class.java))
    }


    private fun displayNeverAskAgainDialog() {
        val builder =
            AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.label_location_title))
        builder.setMessage(
            getString(R.string.label_location_message)
        )
        builder.setCancelable(false)
        builder.setPositiveButton(
            getString(R.string.btn_permit_manually)
        ) { dialog, _ ->
            dialog.dismiss()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri =
                Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun checkLocationPermission() {
        if (!hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (sharedPreferences.neverAskAgainSelected(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    displayNeverAskAgainDialog()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        Constants.PERMISSIONS_LOCATION,
                        PERMISSION_ALL
                    )
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    Constants.PERMISSIONS_LOCATION,
                    PERMISSION_ALL
                )
            }
        }
    }
}



