package com.example.submissionexpert1.presentation.implementation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.data.source.local.preferences.ThemePreferences
import com.example.submissionexpert1.presentation.common.Message
import com.example.submissionexpert1.presentation.ui.shared.MainButton
import com.example.submissionexpert1.presentation.ui.shared.MainTextField
import com.example.submissionexpert1.presentation.viewmodel.*

@Composable
fun ThemeScreen(
  modifier : Modifier,
  vm : ThemeViewModel = hiltViewModel(),
) {
  val theme by vm.themeMode.collectAsState()
  val onEvent : (ThemeEvent) -> Unit = { vm.onEvent(it) }


  Column(
    modifier = modifier,
  ) {

    ThemeOption(
      title = "Light Mode",
      selected = ThemePreferences.THEME_LIGHT == theme,
      onClick = { onEvent(ThemeEvent.SetThemeMode(ThemePreferences.THEME_LIGHT)) }
    )

    ThemeOption(
      title = "Dark Mode",
      selected = ThemePreferences.THEME_DARK == theme,
      onClick = { onEvent(ThemeEvent.SetThemeMode(ThemePreferences.THEME_DARK)) }
    )

    ThemeOption(
      title = "Follow System",
      selected = ThemePreferences.THEME_SYSTEM == theme,
      onClick = { onEvent(ThemeEvent.SetThemeMode(ThemePreferences.THEME_SYSTEM)) }
    )


  }
}

@Composable
private fun ThemeOption(
  title : String,
  selected : Boolean,
  onClick : () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick),
    verticalAlignment = Alignment.CenterVertically
  ) {
    RadioButton(
      selected = selected,
      onClick = onClick
    )
    Text(text = title)
  }
}

