package com.example.submissionexpert1.presentation.ui.shared.movie

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.R
import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.domain.common.state.ErrorState
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.presentation.ui.state.alert.BottomAlert
import com.example.submissionexpert1.presentation.ui.state.empty.MainEmpty
import com.example.submissionexpert1.presentation.ui.state.error.MainError
import com.example.submissionexpert1.presentation.ui.state.loading.CenteredCircularLoading
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieList(
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
  userId : Long? = null,
  searchedOnce : Boolean = true,

  ) {
  Box(


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
      userId = userId,
      searchedOnce = searchedOnce
    )
    BottomAlert(
      message = alert ?: "",
      onDismiss = { onDismissedAlert() },
      visible = alert != null,
      modifier = Modifier
        
        .align(Alignment.BottomCenter)
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
  userId : Long?,
  searchedOnce : Boolean = true

) {
  SwipeRefresh(
    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
    onRefresh = { onRefresh() },
    modifier = Modifier.padding(
      top = 20.dp,
    )
  ) {
    when {
      isLoading && ! isRefreshing                                                                           -> {
        CenteredCircularLoading(modifier = Modifier.fillMaxSize())
      }

      ! error?.message.isNullOrEmpty() && ! isRefreshing                                                    -> {
        MainError(
          message = error?.message ?: ErrorMessages.UNKNOWN_ERROR,
          onRetry = { onLoad() }
        )
      }


      movies.isEmpty() && ! isLoading && error == null && searchedOnce && ! isLoadingMore && ! isRefreshing -> {
        MainEmpty(
          title = "No movies found",
          description = "Try searching for another movie",
          imgRes = R.drawable.empty
        )
      }

      ! searchedOnce                                                                                        -> {
        MainEmpty(
          title = "Search for movies",
          description = "Type in the search bar above to find movies",
          imgRes = R.drawable.no_result
        )
      }

      else                                                                                                  -> {

        LazyColumn(
          state = listState,
          verticalArrangement = Arrangement.spacedBy(16.dp),
          modifier = Modifier
            .fillMaxHeight()


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
