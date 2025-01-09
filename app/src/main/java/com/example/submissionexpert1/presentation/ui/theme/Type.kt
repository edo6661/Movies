package com.example.submissionexpert1.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.submissionexpert1.R

val Poppins = FontFamily(
  Font(R.font.poppins_regular, FontWeight.Normal),
  Font(R.font.poppins_medium, FontWeight.W500),
  Font(R.font.poppins_semibold, FontWeight.W600),
  Font(R.font.poppins_bold, FontWeight.W700)
)


val Typography = Typography(
  titleLarge = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W700,
    fontSize = 28.sp,
    lineHeight = 33.6.sp,
  ),
  bodyLarge = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W600,
    fontSize = 16.sp,
    lineHeight = 22.4.sp,
  ),
  displayLarge = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W700,
    fontSize = 24.sp,
    lineHeight = 28.8.sp,
  ),
  displaySmall = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W700,
    fontSize = 16.sp,
    lineHeight = 19.2.sp,
  ),
  bodySmall = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp,
    lineHeight = 16.8.sp,
  ),
  titleSmall = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W600,
    fontSize = 20.sp,
    lineHeight = 24.sp,
  ),
  bodyMedium = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W500,
    fontSize = 16.sp,
    lineHeight = 22.4.sp,
  ),
  titleMedium = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W600,
    fontSize = 24.sp,
    lineHeight = 28.8.sp,
  ),
  headlineLarge = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W600,
    fontSize = 20.sp,
    lineHeight = 24.sp,
  ),
  headlineSmall = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W600,
    fontSize = 16.sp,
    lineHeight = 19.2.sp,
  ),
  headlineMedium = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.W600,
    fontSize = 18.sp,
    lineHeight = 21.6.sp,
  ),


  )