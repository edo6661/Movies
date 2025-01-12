package com.example.submissionexpert1.presentation.ui.nav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.shared.MainText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBackNav(
  title : String,
  onNavigateBack : () -> Unit,
) {

  TopAppBar(
    colors = TopAppBarColors(
      containerColor = MaterialTheme.colorScheme.background,
      actionIconContentColor = MaterialTheme.colorScheme.onBackground,
      navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
      scrolledContainerColor = MaterialTheme.colorScheme.background,
      titleContentColor = MaterialTheme.colorScheme.onBackground,
    ),
    title = {
      Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
      ) {
        Icon(
          Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          modifier = Modifier
            .clickable {
              onNavigateBack()
            }
            .padding(16.dp)
            .size(24.dp)
        )

        MainText(
          text = title,
          textSize = Size.Large,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)


        )


      }
    },
    navigationIcon = {

    }
  )

}