package com.example.submissionexpert1.presentation.ui.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.shared.MainText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBackNav(
  title : String,
  onNavigateBack : () -> Unit
) {

  TopAppBar(
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Icon(
          Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          modifier = Modifier
            .padding(16.dp)
            .size(24.dp)
        )

        MainText(
          text = title,
          textSize = Size.Medium,

          )
        Icon(
          Icons.Default.Favorite,
          contentDescription = "Favorite",
          modifier = Modifier
            .padding(16.dp)
            .size(24.dp)
        )

      }
    },
    navigationIcon = {

    }
  )

}