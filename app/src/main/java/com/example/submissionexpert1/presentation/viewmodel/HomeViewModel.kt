package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.data.db.EntertainmentDb
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.common.state.ErrorState
import com.example.submissionexpert1.domain.model.PaginationMovie
import com.example.submissionexpert1.domain.usecase.movie.IGetPopularMoviesUseCase
import com.example.submissionexpert1.domain.usecase.movie.IToggleFavoriteMovieUseCase
import com.example.submissionexpert1.presentation.utils.avoidSameMovieId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getPopularMovieUseCase : IGetPopularMoviesUseCase,
  private val toggleFavoriteMovieUseCase : IToggleFavoriteMovieUseCase,
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
  private val userPreferences : UserPreferences,
  private val db : EntertainmentDb
) : ViewModel() {

  private val _uiState = MutableStateFlow(HomeState())
  val uiState = _uiState.asStateFlow()
  private val _movieState = MutableStateFlow(HomeMovieState())
  val movieState = _movieState.asStateFlow()

  private val isStillOnFirstPage : Boolean
    get() = _uiState.value.page == 1


  init {

    // TODO: untuk testing
//    deleteAllOnDb()

    onEvent(HomeEvent.OnLoad)
    getUserId()
  }

  private fun deleteAllOnDb() {
    viewModelScope.launch(ioDispatcher) {
      db.clearDatabase()
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


  fun onEvent(event : HomeEvent) {
    when (event) {
      is HomeEvent.OnLoad           -> onLoad()
      is HomeEvent.OnRefresh        -> onRefresh()

      is HomeEvent.OnDismissedAlert -> onAlertDismissed()
      is HomeEvent.OnToggleFavorite -> onToggleFavorite(event.movieId)

    }
  }

  private fun onToggleFavorite(movieId : Int) {
    viewModelScope.launch(ioDispatcher) {
      val result = toggleFavoriteMovieUseCase(movieId)
      withContext(mainDispatcher) {
        when (result) {
          is Result.Success -> {
            _uiState.update {
              it.copy(
                isLoadingToggleFavorite = false,
              )
            }
            updateMovieFavoriteStatus(movieId)


          }


          is Result.Error   -> {
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

  private fun onLoad() {

    getPopularMovieUseCase(
      page = _uiState.value.page.toString()
    )
      // * rangkuman akuh
      // ! semua operasi diatas `flowOn` akan dijalankan di background thread
      .flowOn(ioDispatcher)
      // ! semua operasi dibawah `flowOn` akan dijalankan di main thread

      // ! di jalanin sebelum data pertama kali di alirkan
      .onStart {
        handleLoading()
      }

      // ! dijalanin saat data pertama kali di alirkan
      .onEach { result ->
        when (result) {
         

          is Result.Success -> {


            handleSuccessOnLoad(result)

          }

          is Result.Error   -> {
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
      // TODO: REMOVE
//      delay(1000)
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

  private suspend fun handleSuccessOnLoad(
    result : Result.Success<PaginationMovie>
  ) {

    // TODO: REMOVE
//    delay(1000)
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
      message == ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE || message == ErrorMessages.CANT_FETCH_MORE -> {
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

      else                                                                                                   -> {
        _uiState.update {
          it.copy(
            isLoading = false,
            error = ErrorState(message = message),
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
        error = ErrorState(message = message),
        page = 1,

        )
    }
  }

}

data class HomeState(
  val isLoading : Boolean = true,
  val isRefreshing : Boolean = false,
  val isLoadingMore : Boolean = false,
  val page : Int = 1,
  val error : ErrorState? = null,
  val alert : String? = null,
  val isLoadingToggleFavorite : Boolean = false,
  val userId : Long? = null
)

data class HomeMovieState(
  val data : PaginationMovie? = null,
  val dataBeforeRefresh : PaginationMovie? = null,
)


sealed class HomeEvent {
  data object OnLoad : HomeEvent()
  data object OnRefresh : HomeEvent()
  data object OnDismissedAlert : HomeEvent()
  data class OnToggleFavorite(
    val movieId : Int
  ) : HomeEvent()
}
