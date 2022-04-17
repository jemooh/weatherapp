package com.jkirwa.weatherapp.data.repository

import com.google.common.truth.Truth
import com.jkirwa.weatherapp.data.currentWeather
import com.jkirwa.weatherapp.data.weatherFavouriteList
import com.jkirwa.weatherapp.data.weatherForecastList
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.spekframework.spek2.Spek

class WeatherRepositoryImplTest : Spek({

    val weatherRepository: WeatherRepository = mockk()

    val dispatcher = TestCoroutineDispatcher()

    beforeGroup {
        Dispatchers.setMain(dispatcher = dispatcher)
    }

    group("Fetching local weather and forecast data from db") {

        test("Test that local Forecast weather information is fetched successfully") {
            runBlocking {
                coEvery {
                    weatherRepository.getForecast()
                } returns flowOf(weatherForecastList)
                val results = weatherRepository.getForecast()
                Truth.assertThat(results).isNotNull()
            }
        }

        test("Test that local current weather information is fetched successfully") {
            runBlocking {
                coEvery {
                    weatherRepository.getCurrentWeather()
                } returns flowOf(currentWeather)
                val results = weatherRepository.getCurrentWeather().toList()
                for (i in 0..results.size - 2) {
                    Truth.assertThat(results[i].description).isEqualTo("overcast clouds")
                }
            }
        }

        test("Test that local favourite weather information is fetched successfully") {
            runBlocking {
                coEvery {
                    weatherRepository.getFavouriteWeather()
                } returns flowOf(weatherFavouriteList)
                val results = weatherRepository.getForecast()
                Truth.assertThat(results).isNotNull()
            }
        }
    }
})
