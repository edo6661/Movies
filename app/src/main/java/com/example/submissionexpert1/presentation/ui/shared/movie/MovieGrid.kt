package com.example.submissionexpert1.presentation.ui.shared.movie

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.R
import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.domain.common.state.ErrorState
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.shared.MainText
import com.example.submissionexpert1.presentation.ui.shared.SearchClickableTextField
import com.example.submissionexpert1.presentation.ui.state.alert.BottomAlert
import com.example.submissionexpert1.presentation.ui.state.empty.MainEmpty
import com.example.submissionexpert1.presentation.ui.state.error.MainError
import com.example.submissionexpert1.presentation.ui.state.loading.CenteredCircularLoading
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieGrid(
  movies : List<Movie>,
  gridState : LazyGridState,
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
  navigateToSearch : () -> Unit = {},
  isTopSectionExist : Boolean = true,
  column : Int

) {
  Box(


  ) {
    MovieGridContent(
      movies = movies,
      gridState = gridState,
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
      searchedOnce = searchedOnce,
      navigateToSearch = navigateToSearch,
      isTopSectionExist = isTopSectionExist,
      column = column
    )
    BottomAlert(
      message = alert ?: "",
      onDismiss = { onDismissedAlert() },
      visible = alert.isNullOrEmpty().not(),
      modifier = Modifier.align(Alignment.BottomCenter)
    )

  }
}

@Composable
fun MovieGridContent(
  movies : List<Movie>,
  gridState : LazyGridState,
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
  searchedOnce : Boolean = true,
  navigateToSearch : () -> Unit,
  isTopSectionExist : Boolean = true,
  column : Int

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
          description = "xiexie, next",
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

        LazyVerticalGrid(
          state = gridState,
          verticalArrangement = Arrangement.spacedBy(16.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          columns = GridCells.Fixed(column),
          modifier = Modifier
            .fillMaxHeight()


        ) {
          if (isTopSectionExist) {
            item(
              span = {
                GridItemSpan(column)
              }
            ) {

              MainText(
                text = "What do you want to watch?",
                textSize = Size.ExtraLarge
              )
            }
            item(
              span = {
                GridItemSpan(column)
              }
            ) {
              SearchClickableTextField(
                onSearchClick = {
                  navigateToSearch()

                }
              )
            }
          }
          items(
            count = movies.size,
            key = { index -> movies[index].id }
          ) { i ->
            CardGridMovieItem(
              movie = movies[i],
              onClick = {
                onNavigateDetail(movies[i].id.toString())
              },
              isLoadingToggleFavorite = isLoadingToggleFavorite,
              onToggleFavorite = onToggleFavorite,
              userId = userId
            )
          }


          item(
            span = {
              GridItemSpan(column)
            }

          ) {
            if (isLoadingMore) {
              CenteredCircularLoading(modifier = Modifier.fillMaxSize())
            }
          }
        }
      }
    }
  }
}
