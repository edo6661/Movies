package com.example.submissionexpert1.presentation.implementation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.presentation.ui.state.error.MainError
import com.example.submissionexpert1.presentation.ui.state.loading.CenteredCircularLoading
import com.example.submissionexpert1.presentation.viewmodel.HomeEvent
import com.example.submissionexpert1.presentation.viewmodel.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeScreen(
  modifier : Modifier = Modifier,
  onNavigateDetail : (String) -> Unit,
  viewModel : HomeViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()


  SwipeRefresh(
    state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
    onRefresh = {
      viewModel.onEvent(HomeEvent.OnRefresh)
    },
    modifier = modifier
  ) {
    when {
      state.isLoading && ! state.isRefreshing -> {
        CenteredCircularLoading()
      }

      ! state.error?.message.isNullOrEmpty()  -> {
        MainError(
          message = state.error?.message ?: "Error",
          description = "Ada yang salah dengan sesuatu, coba lagi nanti ya!",
          onRetry = {
            viewModel.onEvent(HomeEvent.OnLoad)
          }
        )
      }

      else                                    -> {
        LazyColumn {
          items(
            items = state.data?.results ?: emptyList(),
            key = { movie -> movie.id },
          ) { movie ->
            Text(
              text = movie.title
            )
          }
        }
      }
    }
  }
}
