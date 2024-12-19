package com.example.submissionexpert1.presentation.implementation.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
  modifier : Modifier,
  onNavigateRegister : () -> Unit
) {
  Box(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .padding(bottom = 32.dp)
  ) {
    TextButton(
      onClick = onNavigateRegister
    ) {
      Text("Goto Screen")
    }
  }

}