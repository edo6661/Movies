package com.example.submissionexpert1.presentation.ui.shared.movie

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.domain.common.state.ErrorState
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.presentation.ui.state.alert.BottomAlert
import com.example.submissionexpert1.presentation.ui.state.error.MainError
import com.example.submissionexpert1.presentation.ui.state.loading.CenteredCircularLoading
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MovieList(
  modifier : Modifier = Modifier,
  movies : List<Movie>,
  listState : LazyListState,
  onNavigateDetail : (String) -> Unit,
  alert : String?,
  isLoading : Boolean,
  isRefreshing : Boolean,
  isLoadingMore : Boolean,
  error : ErrorState?,
  onDismissedAlert : () -> Unit,
  onLoad : () -> Unit,
  onRefresh : () -> Unit,
  isLoadingToggleFavorite : Boolean,
  onToggleFavorite : (Int) -> Unit,
  userId : Long? = null

) {
  Box(
    modifier = modifier
      .padding(horizontal = 16.dp)
  ) {
    MovieListContent(
      movies = movies,
      listState = listState,
      onNavigateDetail = onNavigateDetail,
      isLoading = isLoading,
      isRefreshing = isRefreshing,
      isLoadingMore = isLoadingMore,
      error = error,
      onRefresh = onRefresh,
      onLoad = onLoad,
      isLoadingToggleFavorite = isLoadingToggleFavorite,
      onToggleFavorite = onToggleFavorite,
      userId = userId

    )

    BottomAlert(
      message = alert ?: ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE,
      onDismiss = { onDismissedAlert() },
      visible = alert != null,
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }
}

@Composable
fun MovieListContent(
  movies : List<Movie>,
  listState : LazyListState,
  onNavigateDetail : (String) -> Unit,
  isLoading : Boolean,
  isRefreshing : Boolean,
  isLoadingMore : Boolean,
  error : ErrorState?,
  onRefresh : () -> Unit,
  onLoad : () -> Unit,
  isLoadingToggleFavorite : Boolean,
  onToggleFavorite : (Int) -> Unit,
  userId : Long?

) {
  SwipeRefresh(
    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
    onRefresh = { onRefresh() },
  ) {
    when {
      isLoading && ! isRefreshing -> {
        CenteredCircularLoading(modifier = Modifier.fillMaxSize())
      }

      ! error?.message.isNullOrEmpty() && ! isRefreshing -> {
        MainError(
          message = error?.message ?: ErrorMessages.UNKNOWN_ERROR,
          onRetry = { onLoad() }
        )
      }

      else -> {
        LazyColumn(
          state = listState,
          verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          items(
            items = movies,
            key = { movie -> movie.id }
          ) { movie ->
            CardMovieItem(
              movie = movie,
              onClick = {
                onNavigateDetail(movie.id.toString())
              },
              isLoadingToggleFavorite = isLoadingToggleFavorite,
              onToggleFavorite = onToggleFavorite,
              userId = userId
            )
          }

          item {
            if (isLoadingMore) {
              CenteredCircularLoading(modifier = Modifier.fillMaxWidth())
            }
          }
        }
      }
    }
  }
}
