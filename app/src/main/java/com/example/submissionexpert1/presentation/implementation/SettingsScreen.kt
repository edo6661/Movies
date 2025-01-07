package com.example.submissionexpert1.presentation.implementation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.submissionexpert1.presentation.ui.shared.MainText

@Composable
fun SettingsScreen(
  modifier : Modifier
) {
  Column(modifier = modifier) {
    MainText(
      text = "Settings",
    )
  }
}