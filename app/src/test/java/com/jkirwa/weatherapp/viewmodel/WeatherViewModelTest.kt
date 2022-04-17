package com.jkirwa.weatherapp.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.jkirwa.weatherapp.data.remote.model.Result
import com.jkirwa.weatherapp.data.repository.WeatherRepository
import com.jkirwa.weatherapp.data.testForecastResponseResult
import com.jkirwa.weatherapp.data.testWeatherResponseResult
import com.jkirwa.weatherapp.ui.weather.viewmodel.FavouriteWeatherViewModel
import com.jkirwa.weatherapp.ui.weather.viewmodel.WeatherViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions
import org.spekframework.spek2.Spek

@ExperimentalTime
@ExperimentalCoroutinesApi
internal class WeatherViewModelTest : Spek({

    val weatherRepository = mockk<WeatherRepository>()
    val weatherViewModel by lazy { WeatherViewModel(weatherRepository = weatherRepository) }
    val testLat = "-1.3196"
    val testLon = "36.849"

    val dispatcher = TestCoroutineDispatcher()

    beforeGroup {
        Dispatchers.setMain(dispatcher = dispatcher)
    }

    group("Fetching Current Weather Information") {


        test("Assert that an event was received and return it") {

            runBlocking {
                coEvery {
                    weatherRepository.fetchCurrentLocationWeather(
                        testLat,
                        testLon
                    )
                } returns Result.Success(
                    true
                )
                weatherViewModel.fetchCurrentWeather(testLat, testLon)
                coVerify { weatherRepository.fetchCurrentLocationWeather(testLat, testLon) }
                weatherViewModel.state.test {
                    awaitEvent()
                }
            }
        }

        test("Test that Current weather information is fetched successfully") {

            runBlocking {
                coEvery {
                    weatherRepository.fetchCurrentLocationWeather(
                        testLat, testLon
                    )
                } returns Result.Success(
                    data = true
                )
                weatherViewModel.fetchCurrentWeather(testLat, testLon)
                coVerify { weatherRepository.fetchCurrentLocationWeather(testLat, testLon) }
                weatherViewModel.state.test {
                    Assertions.assertEquals(awaitItem(), testWeatherResponseResult)
                }
            }
        }
    }

    group("Fetching 5 day Forecast Weather Information") {


        test("Assert that an event was received and return it") {

            runBlocking {
                coEvery {
                    weatherRepository.fetch5dayWeatherForecast(
                        testLat,
                        testLon
                    )
                } returns Result.Success(
                    true
                )

                weatherViewModel.fetch5dayWeatherForecast(testLat, testLon)
                coVerify { weatherRepository.fetch5dayWeatherForecast(testLat, testLon) }
                weatherViewModel.state.test {
                    Assertions.assertEquals(awaitItem(), testForecastResponseResult)
                }
            }
        }

        test("Test that Current weather information is fetched successfully") {

            runBlocking {
                coEvery {
                    weatherRepository.fetch5dayWeatherForecast(
                        testLat, testLon
                    )
                } returns Result.Success(
                    data = true
                )
                weatherViewModel.fetch5dayWeatherForecast(testLat, testLon)
                coVerify { weatherRepository.fetch5dayWeatherForecast(testLat, testLon) }
                weatherViewModel.state.test {
                    Assertions.assertEquals(awaitItem(), testForecastResponseResult)
                }
            }
        }
    }

    afterGroup {
        Dispatchers.resetMain()
    }
})
