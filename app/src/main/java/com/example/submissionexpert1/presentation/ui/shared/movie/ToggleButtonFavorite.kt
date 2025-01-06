package com.example.submissionexpert1.presentation.ui.shared.movie

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ToggleButtonFavorite(
  modifier : Modifier = Modifier,
  isFavorite : Boolean,
  isLoadingToggleFavorite : Boolean,
  onToggleFavorite : (Int) -> Unit,
  id : Int
) {
  val color = animateColorAsState(
    targetValue = if (isFavorite) {
      Color.Red
    } else {
      MaterialTheme.colorScheme.onSecondary
    },
    animationSpec = tween(300),
    label = "Favorite Color"
  )
  IconButton(
    onClick = {
      onToggleFavorite(id)
    },
    enabled = ! isLoadingToggleFavorite,
    modifier = modifier
  ) {
    Icon(
      imageVector = Icons.Default.Favorite,
      contentDescription = "Favorite",
      tint = color.value
    )
  }
}