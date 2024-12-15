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
import com.example.submissionexpert1.core.constants.AlertMessages
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



  Box(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .padding(
        bottom = 32.dp
      )
  ) {


    SwipeRefresh(
      state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
      onRefresh = { viewModel.onEvent(HomeEvent.OnRefresh) },
    ) {

      when {
        state.isLoading && ! state.isRefreshing -> {
          CenteredCircularLoading(
            modifier = Modifier.fillMaxSize()
          )
        }

        ! state.error?.message.isNullOrEmpty() && ! state.isRefreshing -> {
          MainError(
            message = state.error?.message ?: ErrorMessages.UNKNOWN_ERROR,
            onRetry = { viewModel.onEvent(HomeEvent.OnLoad) }
          )
        }

        else -> {
          val itemsBasedOnRefresh = if (state.isRefreshing) {
            state.previousData?.results ?: emptyList()
          } else {
            state.data?.results ?: emptyList()
          }
          LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {
            item {
              MainButton(
                text = "Active Alert",
                onClick = {
                  viewModel.onEvent(HomeEvent.OnAlertActive)
                }
              )
            }
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

    AnimatedVisibility(
      visible = state.alert != null,
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
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.medium)
          .padding(16.dp)
      ) {
        MainText(
          text = state.alert ?: AlertMessages.NO_INTERNET_CONNECTION_ONLY_CACHE,
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
