package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cori.constants.ErrorMessages
import com.example.domain.event.FavoriteChangeEvent
import com.example.domain.usecase.movie.IGetPopularMoviesFavoriteUseCase
import com.example.domain.usecase.movie.IToggleFavoriteMovieUseCase
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import com.example.submissionexpert1.presentation.utils.avoidSameMovieId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
  private val getPopularMovieUseCase : IGetPopularMoviesFavoriteUseCase,
  private val toggleFavoriteMovieUseCase : IToggleFavoriteMovieUseCase,
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
  private val userPreferences : UserPreferences,
  private val movieRepository : com.example.domain.repository.movie.IMovieRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(FavoriteState())
  val uiState = _uiState.asStateFlow()
  private val _movieState = MutableStateFlow(FavoriteMovieState())
  val movieState = _movieState.asStateFlow()

  private val isStillOnFirstPage : Boolean
    get() = _uiState.value.page == 1


  init {


    onEvent(FavoriteEvent.OnLoad)
    getUserId()
    observeFavoriteChanges()
  }

  private fun observeFavoriteChanges() {
    viewModelScope.launch {
      movieRepository.favoriteChanges.collect { e ->
        when (e) {
          is FavoriteChangeEvent.Toggled -> {
            updateToggledMovie(e.movieId)
          }


        }
      }
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


  fun onEvent(event : FavoriteEvent) {
    when (event) {
      is FavoriteEvent.OnLoad           -> onLoad()
      is FavoriteEvent.OnRefresh        -> onRefresh()

      is FavoriteEvent.OnDismissedAlert -> onAlertDismissed()
      is FavoriteEvent.OnToggleFavorite -> onToggleFavorite(event.movieId)

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
            updateToggledMovie(movieId)


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

  private fun updateToggledMovie(
    movieId : Int
  ) {
    _movieState.update { currentMovieState ->
      currentMovieState.data?.let { currentData ->
        val updatedData = currentData.copy(
          results = currentData.results.filter {
            it.id != movieId
          }
        )
        currentMovieState.copy(
          data = updatedData
        )
      } ?: currentMovieState

    }
  }

  private fun onLoad() {

    getPopularMovieUseCase(
      page = _uiState.value.page.toString()
    )
      .flowOn(ioDispatcher)

      .onStart {
        handleLoading()
      }

      .onEach { result ->
        when (result) {
          is com.example.domain.common.Result.Success -> {
            handleSuccess(result)

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
      onLoad()
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
        alert = null
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
              error = null
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

  private suspend fun handleSuccess(
    result : com.example.domain.common.Result.Success<com.example.domain.model.PaginationMovie>
  ) {

    val data =
      avoidSameMovieId(
        dispatcher = ioDispatcher,
        currentData = _movieState.value.data,
        incomingData = result.data
      )
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
            alert = message,
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

data class FavoriteState(
  val isLoading : Boolean = true,
  val isRefreshing : Boolean = false,
  val isLoadingMore : Boolean = false,
  val page : Int = 1,
  val error : com.example.domain.common.state.ErrorState? = null,
  val alert : String? = null,
  val isLoadingToggleFavorite : Boolean = false,
  val userId : Long? = null
)

data class FavoriteMovieState(
  val data : com.example.domain.model.PaginationMovie? = null,
  val dataBeforeRefresh : com.example.domain.model.PaginationMovie? = null,
)


sealed class FavoriteEvent {
  data object OnLoad : FavoriteEvent()
  data object OnRefresh : FavoriteEvent()
  data object OnDismissedAlert : FavoriteEvent()
  data class OnToggleFavorite(
    val movieId : Int
  ) : FavoriteEvent()
}
