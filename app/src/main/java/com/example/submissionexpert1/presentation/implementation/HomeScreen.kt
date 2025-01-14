package com.example.submissionexpert1.presentation.implementation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.shared.MainText
import com.example.submissionexpert1.presentation.ui.shared.SearchClickableTextField
import com.example.submissionexpert1.presentation.ui.shared.movie.MovieGrid
import com.example.submissionexpert1.presentation.viewmodel.HomeEvent
import com.example.submissionexpert1.presentation.viewmodel.HomeViewModel

private const val LOAD_MORE_THRESHOLD = 3

@Composable
fun HomeScreen(
  modifier : Modifier = Modifier,
  onNavigateDetail : (String) -> Unit,
  viewModel : HomeViewModel = hiltViewModel(),
  navigateToLogin : () -> Unit,
  navigateToSearch : () -> Unit

) {
  val uiState by viewModel.uiState.collectAsState()
  val movieState by viewModel.movieState.collectAsState()
  val onEvent = { event : HomeEvent -> viewModel.onEvent(event) }
  val movies = if (uiState.isRefreshing) {
    movieState.dataBeforeRefresh?.results ?: emptyList()
  } else {
    movieState.data?.results ?: emptyList()
  }

  val gridState = rememberLazyGridState()

  val reachedBottom by remember {
    derivedStateOf {
      val layoutInfo = gridState.layoutInfo
      val totalItemsCount = layoutInfo.totalItemsCount
      val lastVisibleItemIndex =
        (gridState.firstVisibleItemIndex + layoutInfo.visibleItemsInfo.size)

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
    gridState.scrollToItem(0)
  }

  Column(
    modifier = modifier.padding(
      16.dp
    ),
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    MainText(
      text = "What do you want to watch?",
      textSize = Size.ExtraLarge
    )

    SearchClickableTextField(
      onSearchClick = {
        navigateToSearch()

      }
    )


    MovieGrid(
      movies = movies,
      gridState = gridState,
      onNavigateDetail = onNavigateDetail,
      alert = uiState.alert,
      isLoading = uiState.isLoading,
      isRefreshing = uiState.isRefreshing,
      isLoadingMore = uiState.isLoadingMore,
      error = uiState.error,
      isLoadingToggleFavorite = uiState.isLoadingToggleFavorite,
      onToggleFavorite = { movieId ->
        if (uiState.userId == null) {
          navigateToLogin()
          return@MovieGrid
        }
        onEvent(HomeEvent.OnToggleFavorite(movieId))
      },
      onDismissedAlert = { onEvent(HomeEvent.OnDismissedAlert) },
      userId = uiState.userId,
      onLoad = { onEvent(HomeEvent.OnLoad) },
      onRefresh = { onEvent(HomeEvent.OnRefresh) },
    )
  }
}
//
//@Composable
//fun HomeContent(
//  uiState : HomeState,
//  movies : List<Movie>,
//  listState : LazyListState,
//  onEvent : (HomeEvent) -> Unit,
//  onNavigateDetail : (String) -> Unit
//
//) {
//  SwipeRefresh(
//    state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
//    onRefresh = { onEvent(HomeEvent.OnRefresh) },
//  ) {
//    when {
//      uiState.isLoading && ! uiState.isRefreshing -> {
//        CenteredCircularLoading(
//          modifier = Modifier.fillMaxSize()
//        )
//      }
//
//      ! uiState.error?.message.isNullOrEmpty() && ! uiState.isRefreshing -> {
//        MainError(
//          message = uiState.error?.message ?: ErrorMessages.UNKNOWN_ERROR,
//          onRetry = { onEvent(HomeEvent.OnLoad) }
//        )
//      }
//
//      else -> {
//
//        LazyColumn(
//          state = listState,
//          verticalArrangement = Arrangement.spacedBy(16.dp),
//
//          ) {
//
//          items(
//            items = movies,
//            key = { movie -> movie.id }
//          ) { movie ->
//            CardMovieItem(
//              movie = movie,
//              onClick = {
//                Log.d("HomeScreen", "Movie: ${movie.title}")
//                onNavigateDetail(movie.id.toString())
//              }
//            )
//          }
//
//          item {
//            // ! kalo state is loading more (sengaja ga di if)
//            // TODO: nanti apa apain
//            CenteredCircularLoading(
//              modifier = Modifier.fillMaxWidth()
//            )
//          }
//        }
//      }
//    }
//  }
//
//
//}