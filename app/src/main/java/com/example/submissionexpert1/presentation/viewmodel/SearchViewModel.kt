package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cori.constants.ErrorMessages
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import com.example.submissionexpert1.presentation.utils.avoidSameMovieId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
  private val getMoviesWithQuery : com.example.domain.usecase.movie.IGetMoviesWithQueryUseCase,
  private val toggleFavoriteMovieUseCase : com.example.domain.usecase.movie.IToggleFavoriteMovieUseCase,
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
  private val userPreferences : UserPreferences,
) : ViewModel() {

  private val _uiState = MutableStateFlow(SearchState())
  val uiState = _uiState.asStateFlow()
  private val _movieState = MutableStateFlow(SearchMovieState())
  val movieState = _movieState.asStateFlow()

  private val isStillOnFirstPage : Boolean
    get() = _uiState.value.page == 1

  private var searchJob : Job? = null


  init {
    getUserId()
    viewModelScope.launch {
      _uiState
        .map { it.query }
        .distinctUntilChanged()
        .debounce(500)
        .collect { query ->
          if (query.isNotEmpty()) {
            resetStates()
            onSearch()
          }
        }
    }
  }

  private fun resetStates() {
    _uiState.update {
      it.copy(
        page = 1,
        isLoading = false,
        isLoadingMore = false,
        isRefreshing = false,
        error = null,
        alert = null
      )
    }
    _movieState.update {
      SearchMovieState(
        data = null,
        dataBeforeRefresh = null
      )
    }
  }


  private fun getUserId() {
    viewModelScope.launch {
      userPreferences.getUserData().collect { user ->
        _uiState.update {
          it.copy(
            userId = user?.userId
          )
        }
      }
    }
  }


  fun onEvent(event : SearchEvent) {
    when (event) {
      is SearchEvent.OnSearch         -> onSearch()
      is SearchEvent.OnRefresh        -> onRefresh()

      is SearchEvent.OnDismissedAlert -> onAlertDismissed()
      is SearchEvent.OnToggleFavorite -> onToggleFavorite(event.movieId)
      is SearchEvent.OnQueryChanged   -> onQueryChanged(event.query)

    }
  }


  private fun onQueryChanged(
    query : String
  ) {
    _uiState.update {
      it.copy(
        query = query
      )
    }
  }

  private fun onToggleFavorite(movieId : Int) {
    viewModelScope.launch(ioDispatcher) {
      val result = toggleFavoriteMovieUseCase(movieId)
      withContext(mainDispatcher) {
        when (result) {
          is com.example.domain.common.Result.Success -> {
            _uiState.update {
              it.copy(
                isLoadingToggleFavorite = false,
              )
            }
            updateMovieFavoriteStatus(movieId)


          }


          is com.example.domain.common.Result.Error   -> {
            _uiState.update {
              it.copy(
                alert = result.message,
                isLoadingToggleFavorite = false
              )
            }
          }
        }
      }
    }
  }

  private fun updateMovieFavoriteStatus(
    movieId : Int
  ) {
    _movieState.update { currentMovieState ->
      currentMovieState.data?.let { currentData ->
        val updatedData = currentData.copy(
          results = currentData.results.map { currentMovie ->
            if (currentMovie.id == movieId) {
              currentMovie.copy(
                isFavorite = ! currentMovie.isFavorite
              )
            } else {
              currentMovie
            }
          }
        )
        currentMovieState.copy(
          data = updatedData
        )
      } ?: currentMovieState

    }
  }

  private fun onSearch() {

    searchJob?.cancel()


    if (_uiState.value.query.isNotEmpty()) {
      getMoviesWithQuery(
        page = _uiState.value.page.toString(),
        query = _uiState.value.query
      )
        .flowOn(ioDispatcher)

        .onStart {
          handleLoading()
        }

        .onEach { result ->
          when (result) {


            is com.example.domain.common.Result.Success -> {
              handleSuccessOnSearch(result)

            }


            is com.example.domain.common.Result.Error   -> {
              handleError(result.message)
            }
          }
        }
        .catch { e ->
          handleCatch(e.message ?: "Unknown Error")
        }
        .flowOn(mainDispatcher)
        .launchIn(viewModelScope)
    }

  }


  private fun onRefresh() {
    _uiState.update {
      it.copy(
        isRefreshing = true,
        page = 1,
        isLoading = false,
        isLoadingMore = false,
        error = null,
      )
    }
    _movieState.update {
      it.copy(
        data = null,
        dataBeforeRefresh = it.data
      )
    }

    viewModelScope.launch {
      onSearch()
      _uiState.update {
        it.copy(
          isRefreshing = false,
        )
      }
      _movieState.update {
        it.copy(
          dataBeforeRefresh = null
        )
      }

    }
  }

  private fun onAlertDismissed() {
    _uiState.update {
      it.copy(
        alert = null,
      )
    }
  }

  private fun handleLoading() {
    if (! _uiState.value.isRefreshing) {
      when (isStillOnFirstPage) {
        true  -> {
          _uiState.update {
            it.copy(
              isLoading = true,
              error = null,
              searchedOnce = true
            )
          }
        }

        false -> {
          _uiState.update {
            it.copy(
              isLoadingMore = true,
              error = null
            )
          }
        }
      }
    }
  }

  private suspend fun handleSuccessOnSearch(
    result : com.example.domain.common.Result.Success<com.example.domain.model.PaginationMovie>
  ) {
    val data =
      avoidSameMovieId(
        dispatcher = ioDispatcher,
        currentData = _movieState.value.data,
        incomingData = result.data
      )

    if (_uiState.value.page == 1) {
      _movieState.update {
        it.copy(data = data)
      }
    } else {
      val currentResults = _movieState.value.data?.results ?: emptyList()
      val newResults = currentResults + (data.results)
      _movieState.update {
        it.copy(
          data = data.copy(
            results = newResults
          )
        )
      }

    }
    _uiState.update {
      it.copy(
        isLoading = false,
        isLoadingMore = false,
        page = it.page + 1
      )
    }


    _uiState.update {
      it.copy(
        isLoading = false,
        isLoadingMore = false,
        error = null,
        isRefreshing = false,
        page = it.page + 1,

        )
    }
    _movieState.update {
      it.copy(
        data = data
      )
    }

  }

  private suspend fun handleError(
    message : String
  ) {
    when {
      message == com.example.cori.constants.ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE || message == com.example.cori.constants.ErrorMessages.CANT_FETCH_MORE -> {
        _uiState.update {
          it.copy(
            error = com.example.domain.common.state.ErrorState(message = "No internet connection"),
            isLoading = false,
            isRefreshing = false,
            isLoadingMore = false,
          )
        }
        delay(3000)
        _uiState.update {
          it.copy(
            alert = null
          )
        }
      }

      else                                                                                                                                                         -> {
        _uiState.update {
          it.copy(
            isLoading = false,
            error = com.example.domain.common.state.ErrorState(message = message),
            isRefreshing = false,
            isLoadingMore = false,
            page = 1,

            )
        }
      }
    }

  }

  private fun handleCatch(
    message : String
  ) {
    _uiState.update {
      it.copy(
        isLoading = false,
        isRefreshing = false,
        isLoadingMore = false,
        error = com.example.domain.common.state.ErrorState(message = message),
        page = 1,

        )
    }
  }

}

data class SearchState(
  val isLoading : Boolean = false,
  val isRefreshing : Boolean = false,
  val isLoadingMore : Boolean = false,
  val page : Int = 1,
  val error : com.example.domain.common.state.ErrorState? = null,
  val alert : String? = null,
  val isLoadingToggleFavorite : Boolean = false,
  val userId : Long? = null,
  val query : String = "",
  val searchedOnce : Boolean = false,
)

data class SearchMovieState(
  val data : com.example.domain.model.PaginationMovie? = null,
  val dataBeforeRefresh : com.example.domain.model.PaginationMovie? = null,
)


sealed class SearchEvent {
  data object OnSearch : SearchEvent()
  data object OnRefresh : SearchEvent()
  data object OnDismissedAlert : SearchEvent()
  data class OnToggleFavorite(
    val movieId : Int
  ) : SearchEvent()

  data class OnQueryChanged(
    val query : String
  ) : SearchEvent()

}
