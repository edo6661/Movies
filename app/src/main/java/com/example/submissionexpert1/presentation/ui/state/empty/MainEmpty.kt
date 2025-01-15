package com.example.submissionexpert1.presentation.ui.state.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.shared.MainText

@Composable
fun MainEmpty(
  title : String,
  description : String,
  imgRes : Int
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically),
    modifier = Modifier.fillMaxSize()
  ) {
    Image(
      painter = painterResource(
        imgRes
      ),
      contentDescription = title,
      modifier = Modifier.size(160.dp)
    )
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      MainText(
        text = title,
        textSize = Size.ExtraLarge
      )
      MainText(
        text = description,
        textSize = Size.Medium,
        color = MaterialTheme.colorScheme.surfaceDim
      )

    }

  }
}