package com.jkirwa.weatherapp.ui.weather.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.jkirwa.weatherapp.data.local.model.FavouriteWeather
import com.jkirwa.weatherapp.data.local.model.Forecast
import com.jkirwa.weatherapp.ui.weather.theme.Blue200
import com.jkirwa.weatherapp.ui.weather.theme.Yellow500
import com.jkirwa.weatherapp.ui.weather.viewmodel.FavouriteWeatherViewModel
import com.jkirwa.weatherapp.ui.weather.viewmodel.WeatherViewModel
import com.jkirwa.weatherapp.utils.Constants.DEGREE_CELSIUS_SYMBOL
import com.jkirwa.weatherapp.utils.Util.Companion.getCurrentDayOfTheWeek
import com.jkirwa.weatherapp.utils.Util.Companion.getDateLatestUpdated
import com.jkirwa.weatherapp.utils.Util.Companion.getFavouriteDrawable
import com.jkirwa.weatherapp.utils.Util.Companion.getWeatherIconDrawable
import org.koin.androidx.compose.getViewModel
import java.util.*

@Preview
@Composable
fun CurrentLocationWeather() {
    val context = LocalContext.current
    val weatherViewModel = getViewModel<WeatherViewModel>()
    val uiState = weatherViewModel.state.value

    val favouriteWeatherViewModel = getViewModel<FavouriteWeatherViewModel>()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .background(color = Blue200)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Box(
            Modifier
                .height(300.dp)
                .background(color = Yellow500)
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 32.dp)
                    .width(100.dp)
                    .height(100.dp)
                    .align(Alignment.CenterStart),
                painter = rememberDrawablePainter(
                    drawable = getWeatherIconDrawable(
                        context,
                        uiState.weather?.icon
                    )
                ),
                contentDescription = null,
            )
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp)
                    .padding(end = 32.dp)
                    .clickable {
                        val weather = uiState.weather
                        weather?.apply {
                            val favouriteWeather = FavouriteWeather(
                                id,
                                UUID
                                    .randomUUID()
                                    .toString(),
                                dt,
                                locationName = locationName,
                                minTemp = minTemp,
                                maxTemp = maxTemp,
                                temp = temp,
                                lat,
                                lon,
                                icon,
                                description,
                                true
                            )
                            favouriteWeatherViewModel.saveFavouriteWeather(favouriteWeather)
                        }
                    },
                painter = rememberDrawablePainter(
                    getFavouriteDrawable(
                        context,
                        uiState.weather?.isFavourite
                    )
                ),
                contentDescription = null,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .wrapContentWidth()
                    .padding(16.dp)
                    .wrapContentHeight()

            ) {
                Spacer(Modifier.size(32.dp))
                Text(
                    text = uiState.weather?.locationName.toString(),
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    text = uiState.weather?.temp.toString() + DEGREE_CELSIUS_SYMBOL,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = uiState.weather?.description.toString().uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )


            }

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Blue200)
                .wrapContentHeight()

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(color = Blue200)

            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                        .padding(start = 16.dp)
                        .padding(end = 16.dp)
                ) {
                    Temp(uiState.weather?.minTemp.toString(), "min")
                    Temp(uiState.weather?.temp.toString(), "Current")
                    Temp(uiState.weather?.maxTemp.toString(), "max")
                }
                Spacer(Modifier.size(16.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(1.dp),
                    text = ""
                )
                Spacer(Modifier.size(32.dp))
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(
                        items = uiState.forecast,
                        itemContent = {
                            if (it.day != getCurrentDayOfTheWeek()) {
                                ForecastListItems(forecast = it)
                            }
                        })
                }
            }
        }

        if (uiState.weather?.dt != null) {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(end = 16.dp)
                    .padding(start = 16.dp),
                text = String.format(
                    "Last updated %s",
                    getDateLatestUpdated(uiState.weather.dt)
                ),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun Temp(temp: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = temp + DEGREE_CELSIUS_SYMBOL,
        )
        Text(
            text = label,
        )
    }
}


@Composable
fun ForecastListItems(forecast: Forecast) {
    val context = LocalContext.current
    Box(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(end = 16.dp)
            .padding(start = 16.dp)
            .padding(top = 8.dp)
    ) {
        Text(forecast.day, modifier = Modifier.align(Alignment.CenterStart))
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = rememberDrawablePainter(
                drawable = getWeatherIconDrawable(
                    context = context,
                    forecast.icon
                )
            ),
            contentDescription = null,
        )
        Text(
            forecast.temp.toString() + DEGREE_CELSIUS_SYMBOL,
            modifier = Modifier.align(Alignment.CenterEnd)
        )

    }
}