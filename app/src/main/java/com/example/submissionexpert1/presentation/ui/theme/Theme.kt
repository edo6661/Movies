package com.example.submissionexpert1.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.submissionexpert1.data.source.local.preferences.ThemePreferences
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
  background = Charcoal,
  onBackground = White,
  onSecondary = White,
  primary = GrayishGreen,
  secondary = LightBlue,
  tertiary = CharcoalGray,
  surfaceDim = CoolGray,
  onSurface = White,
)

private val LightColorScheme = lightColorScheme(
  primary = GrayishGreen,
  secondary = LightBlue,
  tertiary = CharcoalGray,
  surfaceDim = CoolGray,
  background = White,
  onSecondary = White

 
)

@Composable
fun SubmissionExpert1Theme(
  dynamicColor : Boolean = false,
  themePreferences : ThemePreferences,
  content : @Composable () -> Unit
) {
  val themeMode by themePreferences.getThemeMode()
    .collectAsState(initial = ThemePreferences.THEME_SYSTEM)
  val isDarkTheme = when (themeMode) {
    ThemePreferences.THEME_DARK  -> true
    ThemePreferences.THEME_LIGHT -> false
    else                         -> isSystemInDarkTheme()
  }
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    isDarkTheme                                                    -> DarkColorScheme
    else                                                           -> LightColorScheme
  }
  val systemUiController = rememberSystemUiController()
  val useDarkIcons = ! isSystemInDarkTheme()

  val color = Color.Transparent.copy(
    alpha = 0.1f
  )

  SideEffect {
    // ! top
    systemUiController.setSystemBarsColor(
      color = color,
      darkIcons = useDarkIcons,
    )
    // ! bottom
    systemUiController.setNavigationBarColor(
      color = color,
      darkIcons = useDarkIcons
    )
  }



  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}