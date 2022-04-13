package com.jkirwa.weatherapp.di

import androidx.room.Room
import com.jkirwa.weatherapp.data.local.datasource.WeatherDatabase
import com.jkirwa.weatherapp.data.remote.api.WeatherApiService
import com.jkirwa.weatherapp.data.repository.WeatherRepository
import com.jkirwa.weatherapp.data.repository.WeatherRepositoryImpl
import com.jkirwa.weatherapp.ui.weather.viewmodel.WeatherViewModel
import com.jkirwa.weatherapp.utils.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

const val baseUrl: String = Constants.BASE_URL
const val apiKey: String = Constants.APIKEY

val appModule = module {
    single { createNetworkClient(baseUrl, apiKey) }
    single { (get() as? Retrofit)?.create(WeatherApiService::class.java) }

    single {
        Room.databaseBuilder(
            androidContext(),
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    single {
        get<WeatherDatabase>().weatherDao
    }

    single {
        get<WeatherDatabase>().forecastDao
    }


    factory<WeatherRepository> {
        WeatherRepositoryImpl(
            weatherApiService = get(),
            weatherDao = get(), forecastDao = get()
        )
    }

    viewModel {
        WeatherViewModel(weatherRepository = get())
    }
}