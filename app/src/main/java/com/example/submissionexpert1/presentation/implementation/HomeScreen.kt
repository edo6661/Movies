package com.example.submissionexpert1.presentation.implementation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.presentation.ui.shared.movie.CardMovieItem
import com.example.submissionexpert1.presentation.ui.state.alert.BottomAlert
import com.example.submissionexpert1.presentation.ui.state.error.MainError
import com.example.submissionexpert1.presentation.ui.state.loading.CenteredCircularLoading
import com.example.submissionexpert1.presentation.viewmodel.HomeEvent
import com.example.submissionexpert1.presentation.viewmodel.HomeState
import com.example.submissionexpert1.presentation.viewmodel.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

private const val LOAD_MORE_THRESHOLD = 3

@Composable
fun HomeScreen(
  modifier : Modifier = Modifier,
  onNavigateDetail : (String) -> Unit,
  viewModel : HomeViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsState()
  val movieState by viewModel.movieState.collectAsState()
  val onEvent = { event : HomeEvent -> viewModel.onEvent(event) }
  val movies = if (uiState.isRefreshing) {
    movieState.dataBeforeRefresh?.results ?: emptyList()
  } else {
    movieState.data?.results ?: emptyList()
  }

  val listState = rememberLazyListState()

  val reachedBottom by remember {
    derivedStateOf {
      val layoutInfo = listState.layoutInfo
      val totalItemsCount = layoutInfo.totalItemsCount
      val lastVisibleItemIndex =
        (listState.firstVisibleItemIndex + layoutInfo.visibleItemsInfo.size)

      totalItemsCount > 0 && lastVisibleItemIndex > (totalItemsCount - LOAD_MORE_THRESHOLD)

    }
  }


  LaunchedEffect(reachedBottom) {
    if (reachedBottom && ! uiState.isLoadingMore && ! uiState.isRefreshing) {
      viewModel.onEvent(HomeEvent.OnLoad)
    }
  }
  LaunchedEffect(
    key1 = uiState.isLoading,
    key2 = uiState.alert != null,
  ) {
    listState.scrollToItem(0)
  }

  Box(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .padding(
        bottom = 32.dp
      )
  ) {
    HomeContent(
      uiState = uiState,
      movies = movies,
      listState = listState,
      onEvent = onEvent
    )

    BottomAlert(
      message = uiState.alert ?: ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE,
      onDismiss = { viewModel.onEvent(HomeEvent.OnDismissedAlert) },
      visible = uiState.alert != null,
      modifier = Modifier.align(Alignment.BottomCenter)
    )


  }
}

@Composable
fun HomeContent(
  uiState : HomeState,
  movies : List<Movie>,
  listState : LazyListState,
  onEvent : (HomeEvent) -> Unit

) {
  SwipeRefresh(
    state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
    onRefresh = { onEvent(HomeEvent.OnRefresh) },
  ) {
    when {
      uiState.isLoading && ! uiState.isRefreshing -> {
        CenteredCircularLoading(
          modifier = Modifier.fillMaxSize()
        )
      }

      ! uiState.error?.message.isNullOrEmpty() && ! uiState.isRefreshing -> {
        MainError(
          message = uiState.error?.message ?: ErrorMessages.UNKNOWN_ERROR,
          onRetry = { onEvent(HomeEvent.OnLoad) }
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
                Log.d("HomeScreen", "Movie: ${movie.title}")
              }
            )
          }

          item {
            // ! kalo state is loading more (sengaja ga di if)
            // TODO: nanti apa apain
            CenteredCircularLoading(
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }
    }
  }


}