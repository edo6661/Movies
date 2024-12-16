package com.example.submissionexpert1.presentation.implementation

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.presentation.ui.shared.MainButton
import com.example.submissionexpert1.presentation.ui.shared.MainText
import com.example.submissionexpert1.presentation.ui.shared.movie.CardMovieItem
import com.example.submissionexpert1.presentation.ui.state.error.MainError
import com.example.submissionexpert1.presentation.ui.state.loading.CenteredCircularLoading
import com.example.submissionexpert1.presentation.viewmodel.HomeEvent
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
    if (reachedBottom && ! uiState.isLoadingMore && ! uiState.isRefreshing) {
      viewModel.onEvent(HomeEvent.OnLoad)
    }
  }
  LaunchedEffect(
    key1 = uiState.isLoading,
    key2 = uiState.alert != null,
    key3 = uiState.error?.message != null,
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


    SwipeRefresh(
      state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
      onRefresh = { viewModel.onEvent(HomeEvent.OnRefresh) },
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
            onRetry = { viewModel.onEvent(HomeEvent.OnLoad) }
          )
        }

        else -> {
          val itemsBasedOnRefresh = if (uiState.isRefreshing) {
            movieState.dataBeforeRefresh?.results ?: emptyList()
          } else {
            movieState.data?.results ?: emptyList()
          }
          LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {

            items(
              items = itemsBasedOnRefresh,
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
                uiState.isLoadingMore,
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

    AnimatedVisibility(
      visible = uiState.alert != null,
      modifier = Modifier
        .align(Alignment.BottomCenter),
      enter = slideInVertically { it + 200 } + fadeIn(
        initialAlpha = 0.5f,
        animationSpec = tween(durationMillis = 300)
      ),
      exit = slideOutVertically { it + 200 } + fadeOut(
        animationSpec = tween(durationMillis = 300),
        targetAlpha = 0.5f
      )
    ) {
      Log.d("HomeScreen", "Alert: ${uiState.alert}")
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.medium)
          .padding(16.dp)
      ) {
        MainText(
          text = uiState.alert ?: ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        MainButton(
          text = "Dismiss",
          onClick = {
            viewModel.onEvent(HomeEvent.OnAlertDismissed)
          },
          modifier = Modifier.align(Alignment.End),
        )
      }
    }
  }
}
