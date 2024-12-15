package com.example.submissionexpert1.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.data.db.EntertainmentDb
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.common.state.ErrorState
import com.example.submissionexpert1.domain.model.PaginationMovie
import com.example.submissionexpert1.domain.usecase.movie.IGetPopularMoviesUseCase
import com.example.submissionexpert1.presentation.utils.avoidSameMovieId
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
//    viewModelScope.launch(ioDispatcher) {
//      db.clearDatabase()
//    }
    onEvent(HomeEvent.OnLoad)
  }

  fun onEvent(event : HomeEvent) {
    when (event) {
      is HomeEvent.OnLoad           -> loadMovies()
      is HomeEvent.OnRefresh        -> onRefresh()

      is HomeEvent.OnAlertDismissed -> {
        _uiState.update {
          it.copy(
            alert = null
          )
        }

      }
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
      // TODO: REMOVE
      delay(1000)
      loadMovies()
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

  private fun loadMovies() {

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
          is Result.Loading -> {
            handleLoading()
          }

          is Result.Alert   -> {
            _uiState.update {
              it.copy(
                alert = result.message,
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

          is Result.Success -> {

            // TODO: REMOVE
            delay(1000)
            val data =
              avoidSameMovieId(
                dispatcher = ioDispatcher,
                currentData = _movieState.value.data,
                incomingData = result.data
              )
            _uiState.update {
              Log.d("HomeViewModel", "thread: ${Thread.currentThread().name}")
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

          is Result.Error   -> {
            _uiState.update {
              it.copy(
                isLoading = false,
                error = ErrorState(message = result.message),
                isRefreshing = false,
                isLoadingMore = false,
                page = 1,

                )
            }
          }
        }
      }
      .catch { exception ->
        _uiState.update {
          it.copy(
            isLoading = false,
            isRefreshing = false,
            isLoadingMore = false,
            error = ErrorState(message = exception.message),
            page = 1,

            )
        }
      }
      // ! semua perubahan uiState dijalankan di main thread + memastikan aliran data aware terhadap lifecycle
      .launchIn(viewModelScope)
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

}

data class HomeState(
  val isLoading : Boolean = true,
  val isRefreshing : Boolean = false,
  val isLoadingMore : Boolean = false,
  val page : Int = 1,
  val error : ErrorState? = null,
  val alert : String? = null,
)

data class HomeMovieState(
  val data : PaginationMovie? = null,
  val dataBeforeRefresh : PaginationMovie? = null,
)


sealed class HomeEvent {
  data object OnLoad : HomeEvent()
  data object OnRefresh : HomeEvent()
  data object OnAlertDismissed : HomeEvent()
}
