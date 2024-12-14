package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.common.state.ErrorState
import com.example.submissionexpert1.domain.model.PaginationMovie
import com.example.submissionexpert1.domain.usecase.movie.IGetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getPopularMovieUseCase : IGetPopularMoviesUseCase,
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher
) : ViewModel() {

  private val _state = MutableStateFlow(HomeState())
  val state = _state.asStateFlow()

  init {
    onEvent(HomeEvent.OnLoad)
  }

  fun onEvent(event : HomeEvent) {
    when (event) {
      is HomeEvent.OnLoad    -> loadMovies()
      is HomeEvent.OnRefresh -> onRefresh()

    }
  }

  private fun onRefresh() {
    _state.update {
      it.copy(
        isRefreshing = true
      )
    }
    viewModelScope.launch {
      // TODO: REMOVE
      delay(2000)
      loadMovies()
    }
  }

  private fun loadMovies() {
    getPopularMovieUseCase()
      .flowOn(ioDispatcher)
      .onEach { result ->
        when (result) {
          is Result.Loading -> {
            // TODO: REMOVE
            delay(2000)


            _state.update {
              it.copy(
                isLoading = true,
                error = null
              )
            }
          }

          is Result.Success -> {
            _state.update {
              it.copy(
                isLoading = false,
                data = result.data,
                error = null,
                isRefreshing = false
              )
            }
          }

          is Result.Error   -> {
            _state.update {
              it.copy(
                isLoading = false,
                error = ErrorState(message = result.message),
                isRefreshing = false
              )
            }
          }
        }
      }
      .catch { exception ->
        _state.update {
          it.copy(
            isLoading = false,
            error = ErrorState(message = exception.message)
          )
        }
      }
      .launchIn(viewModelScope)
  }
}

data class HomeState(
  val isLoading : Boolean = true,
  val isRefreshing : Boolean = false,
  val error : ErrorState? = null,
  val data : PaginationMovie? = null
)

sealed class HomeEvent {
  object OnLoad : HomeEvent()
  object OnRefresh : HomeEvent()
}
