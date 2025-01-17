package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.Result
import com.example.domain.usecase.movie.IGetMovieUseCase
import com.example.domain.usecase.movie.IToggleFavoriteMovieUseCase
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  savedStateHandle : SavedStateHandle,
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
  private val getMovieUseCase : IGetMovieUseCase,
  private val toggleMovieUseCase : IToggleFavoriteMovieUseCase,
  private val userPreferences : UserPreferences
) : ViewModel() {

  val id : String = savedStateHandle["id"] ?: ""
  private val _state = MutableStateFlow(DetailState())
  val state = _state.asStateFlow()

  init {
    onEvent(DetailEvent.OnMovieLoaded(id.toInt()))
    getUserId()

  }

  private fun getUserId() {
    viewModelScope.launch {
      userPreferences.getUserData().collect { user ->
        _state.update {
          it.copy(
            userId = user?.userId
          )
        }
      }
    }
  }


  fun onEvent(event : DetailEvent) {
    when (event) {
      is DetailEvent.OnMovieLoaded           -> onMovieLoaded(event.id)
      is DetailEvent.OnToggleFavorite        -> onToggleFavorite(event.id)
      is DetailEvent.OnToggleShowAllOverview -> _state.value = _state.value.copy(
        showAllOverview = ! _state.value.showAllOverview
      )
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
              movie = _state.value.movie?.movie?.copy(
                isFavorite = ! _state.value.movie?.movie?.isFavorite !!
              )?.let {
                _state.value.movie?.copy(
                  movie = it
                )
              }
            )
          }

          is Result.Error   -> {
            _state.value = _state.value.copy(
              error = result.message,
              isLoadingToggleFavorite = false
            )
          }


        }
      }

    }

  }

  private fun onMovieLoaded(id : Int) {
    getMovieUseCase(id)
      .flowOn(ioDispatcher)
      .onStart {

        _state.value = _state.value.copy(isLoading = true)
      }
      .onEach {
        when (it) {
          is Result.Success -> {
            _state.value = _state.value.copy(
              movie = it.data,
              isLoading = false
            )
          }

          is Result.Error   -> {
            _state.value = _state.value.copy(
              error = it.message,
              isLoading = false
            )
          }


        }
      }
      .flowOn(mainDispatcher)
      .launchIn(viewModelScope)

  }
}

data class DetailState(
  val movie : com.example.domain.model.MovieWithGenres? = null,
  val isLoading : Boolean = false,
  val isLoadingToggleFavorite : Boolean = false,
  val error : String? = null,
  val userId : Long? = null,
  val showAllOverview : Boolean = false
)

sealed class DetailEvent {
  data class OnMovieLoaded(val id : Int) : DetailEvent()
  data class OnToggleFavorite(val id : Int) : DetailEvent()
  data object OnToggleShowAllOverview : DetailEvent()
}