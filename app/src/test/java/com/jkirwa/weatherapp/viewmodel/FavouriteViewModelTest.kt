package com.jkirwa.weatherapp.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.jkirwa.weatherapp.data.local.model.FavouriteWeather
import com.jkirwa.weatherapp.data.repository.WeatherRepository
import com.jkirwa.weatherapp.data.testFetchFavouriteWeatherResult
import com.jkirwa.weatherapp.data.weatherFavouriteList
import com.jkirwa.weatherapp.ui.weather.viewmodel.FavouriteWeatherViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions
import org.spekframework.spek2.Spek

@ExperimentalTime
@ExperimentalCoroutinesApi
internal class FavouriteViewModelTest : Spek({

    val weatherRepository = mockk<WeatherRepository>()
    val favouriteWeatherViewModel by lazy { FavouriteWeatherViewModel(weatherRepository = weatherRepository) }

    val dispatcher = TestCoroutineDispatcher()

    beforeGroup {
        Dispatchers.setMain(dispatcher = dispatcher)
    }

    group("Fetching Favourite Weather Information") {


        test("Test Saving Favourite weather to db") {

            runBlocking {
                val favourite = FavouriteWeather(
                    184736, "3440",
                    1650209951,
                    "Nairobi South",
                    19,
                    30,
                    24,
                    -1.3188,
                    36.8511,
                    "09n",
                    "overcast clouds",
                    true
                )

                coEvery {
                    favouriteWeatherViewModel.saveFavouriteWeather(favourite)
                } returns Unit

                coEvery { weatherRepository.getFavouriteWeather() } returns flowOf(
                    weatherFavouriteList
                )
                val favouriteWeather = weatherRepository.getFavouriteWeather()
                Truth.assertThat(favouriteWeather).isNotNull()

            }
        }

        test("Test is fetch favourite weather information is fetched successfully") {
            runBlocking {
                coEvery {
                    weatherRepository.getFavouriteWeather()
                } returns flowOf(weatherFavouriteList)
                val result = weatherRepository.getFavouriteWeather().toList()
                Truth.assertThat(result).isNotEmpty()
            }
        }
    }

    afterGroup {
        Dispatchers.resetMain()
    }
})
