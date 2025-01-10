package com.example.submissionexpert1.presentation.ui.shared.movie

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.presentation.ui.shared.MainText
import com.example.submissionexpert1.presentation.ui.theme.Orange

@Composable
fun DetailRating(
  modifier : Modifier,
  rating : Double
) {
  Row(
    modifier = modifier
      .padding(
        bottom = 16.dp,
        end = 16.dp
      )
      .graphicsLayer {

        clip = true
        renderEffect = BlurEffect(radiusX = 2f, radiusY = 2f)
      }
      .background(
        MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.8f),
        shape = RoundedCornerShape(
          16.dp
        )
      )
      .padding(
        vertical = 8.dp,
        horizontal = 16.dp
      ),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = Icons.Default.StarBorder,
      contentDescription = "Rating",
      tint = Orange
    )
    MainText(
      text = DecimalFormat("#.#").format(rating),
    )


  }
}