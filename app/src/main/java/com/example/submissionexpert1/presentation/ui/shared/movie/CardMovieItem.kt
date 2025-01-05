package com.example.submissionexpert1.presentation.ui.shared.movie

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.presentation.ui.shared.MainText


@Composable
fun CardMovieItem(
  modifier : Modifier = Modifier,
  movie : Movie,
  onClick : () -> Unit,
  isLoadingToggleFavorite : Boolean,
  onToggleFavorite : (Int) -> Unit,
  userId : Long? = null

) {
//  val isActive =
  Card(
    modifier = modifier
      .fillMaxWidth(),
    onClick = onClick,
    colors = CardColors(
      containerColor = MaterialTheme.colorScheme.secondaryContainer,
      contentColor = MaterialTheme.colorScheme.onSecondary,
      disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
      disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f),
    ),
    shape = MaterialTheme.shapes.medium,
    elevation = CardDefaults.cardElevation(
      defaultElevation = 16.dp,
      pressedElevation = 8.dp,
      disabledElevation = 0.dp,
      focusedElevation = 16.dp,
      hoveredElevation = 16.dp,
      draggedElevation = 16.dp,
    ),
    border = BorderStroke(
      width = 2.dp,
      color = MaterialTheme.colorScheme.onSecondary.copy(
        alpha = 0.5f
      )
    ),
  ) {
    Column(
      modifier = Modifier.padding(
        vertical = 20.dp,
        horizontal = 16.dp
      )
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        MainText(
          text = movie.title,
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
          onClick = {
            onToggleFavorite(movie.id)
          },
          enabled = ! isLoadingToggleFavorite
        ) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Favorite",
            tint = if (movie.isFavorite) {
              Color.Red
            } else {
              Color.Blue
            }
          )
        }
      }

    }

  }
}
