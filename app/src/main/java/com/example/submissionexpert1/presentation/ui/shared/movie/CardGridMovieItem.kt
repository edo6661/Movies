package com.example.submissionexpert1.presentation.ui.shared.movie

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.cori.constants.Prefix
import com.example.domain.model.Movie
import com.example.submissionexpert1.R


@Composable
fun CardGridMovieItem(
  modifier : Modifier = Modifier,
  movie : Movie,
  onClick : () -> Unit,

  ) {
  Card(
    modifier = modifier
      .fillMaxWidth(),
    onClick = onClick,
    colors = CardColors(
      containerColor = Color.Transparent,
      contentColor = MaterialTheme.colorScheme.onSecondary,
      disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
      disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f),

      ),
    shape = MaterialTheme.shapes.medium,


    ) {
    AsyncImage(
      model = ImageRequest.Builder(LocalContext.current)
        .data(Prefix.PREFIX_IMAGE_URL + movie.posterPath)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .diskCacheKey(movie.posterPath)
        .memoryCacheKey(movie.posterPath)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build(),
      contentDescription = movie.title,
      placeholder = painterResource(id = R.drawable.placeholder),
      error = painterResource(id = R.drawable.error_image),
      modifier = Modifier
        .fillMaxWidth()
        .clip(
          RoundedCornerShape(
            topStart = 8.dp,
            bottomStart = 8.dp,
          )
        )
        .aspectRatio(2 / 3f),
      contentScale = ContentScale.FillHeight
    )
  }

}

