package com.example.submissionexpert1.presentation.implementation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.submissionexpert1.presentation.ui.shared.MainText

@Composable
fun SearchScreen(
  modifier : Modifier,
  onNavigateDetail : (String) -> Unit,
) {
  Column(modifier = modifier) {
    MainText(
      text = "Search",
    )
  }
}