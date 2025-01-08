package com.example.submissionexpert1.presentation.implementation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.presentation.ui.shared.movie.MovieList
import com.example.submissionexpert1.presentation.viewmodel.FavoriteEvent
import com.example.submissionexpert1.presentation.viewmodel.FavoriteViewModel


private const val LOAD_MORE_THRESHOLD = 3

@Composable
fun FavoriteScreen(
  modifier : Modifier = Modifier,
  onNavigateDetail : (String) -> Unit,
  viewModel : FavoriteViewModel = hiltViewModel(),

  ) {
  val uiState by viewModel.uiState.collectAsState()
  val movieState by viewModel.movieState.collectAsState()
  val onEvent = { event : FavoriteEvent -> viewModel.onEvent(event) }
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
      viewModel.onEvent(FavoriteEvent.OnLoad)
    }
  }
  LaunchedEffect(
    key1 = uiState.isLoading,
    key2 = uiState.alert != null,
  ) {
    listState.scrollToItem(0)
  }






  MovieList(
    modifier = modifier,
    movies = movies,
    listState = listState,
    onNavigateDetail = onNavigateDetail,
    alert = uiState.alert,
    isLoading = uiState.isLoading,
    isRefreshing = uiState.isRefreshing,
    isLoadingMore = uiState.isLoadingMore,
    error = uiState.error,
    isLoadingToggleFavorite = uiState.isLoadingToggleFavorite,
    onToggleFavorite = { movieId ->

      onEvent(FavoriteEvent.OnToggleFavorite(movieId))
    },
    onDismissedAlert = { onEvent(FavoriteEvent.OnDismissedAlert) },
    userId = uiState.userId,
    onLoad = { onEvent(FavoriteEvent.OnLoad) },
    onRefresh = { onEvent(FavoriteEvent.OnRefresh) }

  )
}