package com.example.submissionexpert1.presentation.implementation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.presentation.ui.shared.MainTextField
import com.example.submissionexpert1.presentation.ui.shared.movie.MovieList
import com.example.submissionexpert1.presentation.viewmodel.SearchEvent
import com.example.submissionexpert1.presentation.viewmodel.SearchViewModel

private const val LOAD_MORE_THRESHOLD = 3

// TODO: benerin infinite scroll nya di view model dan disini
// ! benerin di onSearch, increment page nya
@Composable
fun SearchScreen(
  modifier : Modifier,
  onNavigateDetail : (String) -> Unit,
  onNavigateBack : () -> Unit,
  vm : SearchViewModel = hiltViewModel()
) {


  val uiState by vm.uiState.collectAsState()
  val movieState by vm.movieState.collectAsState()


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
    if (reachedBottom && ! uiState.isLoadingMore && ! uiState.isRefreshing && uiState.query.isNotEmpty()) {
      vm.onEvent(SearchEvent.OnSearch)
    }
  }
  LaunchedEffect(
    key1 = uiState.isLoading,
    key2 = uiState.alert != null,
  ) {
    listState.scrollToItem(0)
  }




  Column(
    modifier = modifier.padding(horizontal = 16.dp)
  ) {
    MainTextField(
      value = uiState.query,
      onValueChange = {
        vm.onEvent(SearchEvent.OnQueryChanged(it))
      },
      label = "Search Movie",
      trailingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "Search",
          tint = MaterialTheme.colorScheme.tertiary,
        )
      },

      unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
      focusedContainerColor = MaterialTheme.colorScheme.tertiary,
    )


    MovieList(
      movies = movies,
      searchedOnce = uiState.searchedOnce,
      listState = listState,
      onNavigateDetail = onNavigateDetail,
      alert = uiState.alert,
      isLoading = uiState.isLoading,
      isRefreshing = uiState.isRefreshing,
      isLoadingMore = uiState.isLoadingMore,
      error = uiState.error,
      isLoadingToggleFavorite = uiState.isLoadingToggleFavorite,
      onToggleFavorite = { movieId ->
        vm.onEvent(SearchEvent.OnToggleFavorite(movieId))

      },
      onDismissedAlert = {
        vm.onEvent(SearchEvent.OnDismissedAlert)
      },
      userId = uiState.userId,
      onLoad = {
        vm.onEvent(SearchEvent.OnSearch)
      },
      onRefresh = {
        vm.onEvent(SearchEvent.OnRefresh)
      },
      isNoMoreData = uiState.isNoMoreData

    )


  }
}