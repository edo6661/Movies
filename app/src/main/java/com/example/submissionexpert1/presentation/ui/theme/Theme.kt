package com.example.submissionexpert1.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.submissionexpert1.data.source.local.preferences.ThemePreferences

private val DarkColorScheme = darkColorScheme(
  background = Charcoal,
  onBackground = White,
  primary = GrayishGreen,
  secondary = GrayishGreen,
  tertiary = CharcoalGray,
  surfaceDim = CoolGray,
)

private val LightColorScheme = lightColorScheme(
  primary = Purple40,
  secondary = PurpleGrey40,
  tertiary = Pink40

  /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun SubmissionExpert1Theme(
  dynamicColor : Boolean = true,
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

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}