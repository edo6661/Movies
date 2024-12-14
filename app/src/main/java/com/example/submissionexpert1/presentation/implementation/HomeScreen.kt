package com.example.submissionexpert1.presentation.implementation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.presentation.ui.shared.movie.CardMovieItem
import com.example.submissionexpert1.presentation.ui.state.error.MainError
import com.example.submissionexpert1.presentation.ui.state.loading.CenteredCircularLoading
import com.example.submissionexpert1.presentation.viewmodel.HomeEvent
import com.example.submissionexpert1.presentation.viewmodel.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

private const val LOAD_MORE_THRESHOLD = 5

@Composable
fun HomeScreen(
  modifier : Modifier = Modifier,
  onNavigateDetail : (String) -> Unit,
  viewModel : HomeViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()

  val listState = rememberLazyListState()

  val reachedBottom by remember {
    derivedStateOf {
      val layoutInfo = listState.layoutInfo
      val totalItemsCount = layoutInfo.totalItemsCount
      val lastVisibleItemIndex =
        (listState.firstVisibleItemIndex + layoutInfo.visibleItemsInfo.size)

      lastVisibleItemIndex > (totalItemsCount - LOAD_MORE_THRESHOLD)

    }
  }

  LaunchedEffect(reachedBottom) {
    if (reachedBottom && ! state.isLoadingMore && ! state.isRefreshing) {
      viewModel.onEvent(HomeEvent.OnLoad)
    }
  }

  SwipeRefresh(
    state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
    onRefresh = { viewModel.onEvent(HomeEvent.OnRefresh) },
    modifier = modifier
      .padding(horizontal = 16.dp)
      .padding(
        bottom = 32.dp
      )
  ) {
    when {
      state.isLoading && ! state.isRefreshing -> {
        CenteredCircularLoading(
          modifier = Modifier.fillMaxSize()
        )
      }

      ! state.error?.message.isNullOrEmpty()  -> {
        MainError(
          message = state.error?.message ?: "Error",
          description = "Ada yang salah dengan sesuatu, coba lagi nanti ya!",
          onRetry = { viewModel.onEvent(HomeEvent.OnLoad) }
        )
      }

      else                                    -> {
        LazyColumn(
          state = listState,
          verticalArrangement = Arrangement.spacedBy(16.dp),

          ) {
          items(
            items = state.data?.results ?: emptyList(),
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
            AnimatedVisibility(
              state.isLoadingMore,
            ) {
              CenteredCircularLoading(
                modifier = Modifier.fillMaxWidth()
              )
            }
          }
        }
      }
    }
  }
}
