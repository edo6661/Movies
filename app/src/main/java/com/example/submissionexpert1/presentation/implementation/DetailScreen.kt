package com.example.submissionexpert1.presentation.implementation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.submissionexpert1.R
import com.example.submissionexpert1.core.constants.Prefix
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.presentation.ui.shared.MainText
import com.example.submissionexpert1.presentation.ui.shared.movie.ToggleButtonFavorite
import com.example.submissionexpert1.presentation.ui.state.error.MainError
import com.example.submissionexpert1.presentation.ui.state.loading.CenteredCircularLoading
import com.example.submissionexpert1.presentation.viewmodel.DetailEvent
import com.example.submissionexpert1.presentation.viewmodel.DetailViewModel

@Composable
fun DetailScreen(
  modifier : Modifier = Modifier,
  vm : DetailViewModel = hiltViewModel()
) {
  val state by vm.state.collectAsState()
  fun onEvent(event : DetailEvent) {
    vm.onEvent(event)
  }


  Column(
    modifier = modifier.fillMaxSize(),
  ) {
    when {
      state.isLoading -> {
        CenteredCircularLoading(
          modifier = Modifier.fillMaxSize()
        )
      }

      state.error != null -> {
        MainError(
          message = state.error as String,
        )
      }

      state.movie != null -> {
        DetailContent(
          movie = state.movie as Movie,
          onToggleFavorite = { id -> onEvent(DetailEvent.OnToggleFavorite(id)) }
        )
      }
    }

  }
}

@Composable
fun DetailContent(
  movie : Movie,
  onToggleFavorite : (Int) -> Unit
) {

  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    TopSection(
      movie = movie,
      onToggleFavorite = onToggleFavorite
    )
    MiddleSection(
      movie = movie
    )
  }
}

@Composable
private fun TopSection(
  movie : Movie,
  onToggleFavorite : (Int) -> Unit
) {
  Box(
    modifier = Modifier
  ) {

    Image(
      painter = rememberAsyncImagePainter(
        model = Prefix.PREFIX_IMAGE_URL + movie.backdropPath,
        placeholder = painterResource(id = R.drawable.user_placeholder),
        error = painterResource(id = R.drawable.error_image)
      ),
      contentDescription = movie.title,
      contentScale = ContentScale.Inside,
      modifier = Modifier
        .fillMaxWidth()
    )
    ToggleButtonFavorite(
      isFavorite = movie.isFavorite,
      isLoadingToggleFavorite = false,
      onToggleFavorite = onToggleFavorite,
      id = movie.id,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .clip(
          RoundedCornerShape(
            topStart = 4.dp,
          )
        )
        .background(MaterialTheme.colorScheme.secondaryContainer)

    )
  }
}

@Composable
private fun MiddleSection(
  movie : Movie
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.padding(horizontal = 16.dp)
  ) {
    MainText(
      text = movie.title,
    )

  }
}
