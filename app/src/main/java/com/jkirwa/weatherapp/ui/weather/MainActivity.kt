package com.jkirwa.weatherapp.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jkirwa.weatherapp.R
import com.jkirwa.weatherapp.ui.weather.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val weatherViewModel:WeatherViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lat = "-1.3031689499999999"
        val lon = "36.826061224105075"
        weatherViewModel.fetchCurrentWeather(lat,lon)
        weatherViewModel.fetch5dayWeatherForecast(lat,lon)
    }
}