package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.domain.usecase.movie.IGetMovieUseCase
import com.example.submissionexpert1.domain.usecase.movie.IToggleFavoriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  savedStateHandle : SavedStateHandle,
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
  private val getMovieUseCase : IGetMovieUseCase,
  private val toggleMovieUseCase : IToggleFavoriteMovieUseCase
) : ViewModel() {

  val id : String = savedStateHandle["id"] ?: ""
  val _state = MutableStateFlow(DetailState())
  val state = _state.asStateFlow()

  init {
    onEvent(DetailEvent.OnMovieLoaded(id.toInt()))
  }

  fun onEvent(event : DetailEvent) {
    when (event) {
      is DetailEvent.OnMovieLoaded    -> onMovieLoaded(event.id)
      is DetailEvent.OnToggleFavorite -> onToggleFavorite(event.id)
    }
  }

  private fun onToggleFavorite(id : Int) {
    viewModelScope.launch(ioDispatcher) {
      val result = toggleMovieUseCase(id)
      withContext(mainDispatcher) {
        when (result) {
          is Result.Success -> {
            _state.value = _state.value.copy(
              isLoadingToggleFavorite = false,
              movie = _state.value.movie?.copy(
                isFavorite = ! _state.value.movie?.isFavorite !!
              )
            )
          }

          is Result.Error   -> {
            _state.value = _state.value.copy(
              error = result.message,
              isLoadingToggleFavorite = false
            )
          }

          is Result.Loading -> {
            _state.value = _state.value.copy(
              isLoadingToggleFavorite = true
            )
          }
        }
      }

    }

  }

  private fun onMovieLoaded(id : Int) {
    viewModelScope.launch(ioDispatcher) {
      getMovieUseCase(id)
        .onStart {
          withContext(mainDispatcher) {
            _state.value = _state.value.copy(isLoading = true)
          }

        }

        .collect { result ->
          withContext(mainDispatcher) {
            when (result) {
              is Result.Success -> {
                _state.value = _state.value.copy(
                  movie = result.data,
                  isLoading = false
                )
              }

              is Result.Error   -> {
                _state.value = _state.value.copy(
                  error = result.message,
                  isLoading = false
                )
              }

              is Result.Loading -> {
                _state.value = _state.value.copy(
                  isLoading = true
                )
              }
            }
          }
        }
    }
  }
}

data class DetailState(
  val movie : Movie? = null,
  val isLoading : Boolean = false,
  val isLoadingToggleFavorite : Boolean = false,
  val error : String? = null,
)

sealed class DetailEvent {
  data class OnMovieLoaded(val id : Int) : DetailEvent()
  data class OnToggleFavorite(val id : Int) : DetailEvent()
}