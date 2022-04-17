package com.jkirwa.weatherapp.ui.weather.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jkirwa.weatherapp.R

val Chirp = FontFamily(
    Font(R.font.chirp_regular),
    Font(R.font.chirp_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Chirp,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        letterSpacing = 0.5.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Chirp,
        fontWeight = FontWeight.Bold,
        fontSize = 23.sp,
        letterSpacing = 0.sp
    ),
    body1 = TextStyle(
        fontFamily = Chirp,
        fontSize = 16.sp,
        letterSpacing = 0.sp
    ),
    body2 = TextStyle(
        fontFamily = Chirp,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    caption = TextStyle(
        fontFamily = Chirp,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    )

)
