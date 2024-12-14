package com.example.submissionexpert1.presentation.ui.nav

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBackNav(
  title : String,
  onNavigateBack : () -> Unit
) {
  TopAppBar(
    title = {
      Row {
        IconButton(
          onClick = {
            onNavigateBack()
          }
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
          )
        }
        Text(
          text = title
        )
      }
    }
  )

}