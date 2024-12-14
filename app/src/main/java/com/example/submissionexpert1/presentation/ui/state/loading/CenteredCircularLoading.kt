package com.example.submissionexpert1.presentation.ui.state.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CenteredCircularLoading(
  modifier : Modifier
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
  ) {
    CircularProgressIndicator()
  }

}