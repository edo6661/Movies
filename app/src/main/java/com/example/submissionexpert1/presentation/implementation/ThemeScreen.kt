package com.example.submissionexpert1.presentation.implementation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.data.source.local.preferences.ThemePreferences
import com.example.submissionexpert1.presentation.viewmodel.ThemeEvent
import com.example.submissionexpert1.presentation.viewmodel.ThemeViewModel


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

