package com.jkirwa.weatherapp.ui.weather.theme

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import com.jkirwa.weatherapp.R

@Composable
fun TopBar() {

    TopAppBar(
        title = { Text("Weather App") }
    )

      Box {
          Image(
              modifier = Modifier.fillMaxSize(),
              painter = painterResource(R.drawable.background_toolbar_translucent),
              contentDescription = "background_image",
              contentScale = ContentScale.FillBounds
          )
          Scaffold(   // Make the background transparent
              topBar = {
                  TopAppBar(
                      modifier = Modifier
                          .fillMaxHeight(0.2f),
                      title = {
                          Text(text = "Dashboard")
                      }
                  )
              },
          ) {
              // Scaffold content
          }
      }
}

@Preview
@Composable
fun TopBarPreview() {
    WeatherAppTheme {
        TopBar()
    }
}