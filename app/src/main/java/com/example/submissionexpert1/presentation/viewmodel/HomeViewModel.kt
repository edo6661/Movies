package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.core.constants.AlertMessages
import com.example.submissionexpert1.data.db.EntertainmentDb
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
  private val db : EntertainmentDb
) : ViewModel() {

  private val _state = MutableStateFlow(HomeState())
  val state = _state.asStateFlow()

  private val isStillOnFirstPage : Boolean
    get() = _state.value.page == 1


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
        _state.update {
          it.copy(
            alert = null
          )
        }

      }

      is HomeEvent.OnAlertActive    -> {
        viewModelScope.launch {
          _state.update {
            it.copy(
              alert = AlertMessages.NO_INTERNET_CONNECTION_ONLY_CACHE,
            )
          }
          delay(3000)
          _state.update {
            it.copy(
              alert = null
            )
          }
        }
      }

    }
  }

  private fun onRefresh() {
    _state.update {
      it.copy(
        isRefreshing = true,
        page = 1,
        isLoading = false,
        isLoadingMore = false,
        error = null,
        previousData = it.data,
        data = null,

        )
    }
    viewModelScope.launch {
      // TODO: REMOVE
      delay(1000)
      loadMovies()
      _state.update {
        it.copy(
          isRefreshing = false,
          previousData = null
        )
      }

    }
  }

  private fun loadMovies() {

    getPopularMovieUseCase(
      page = _state.value.page.toString()
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
            _state.update {
              it.copy(
                alert = result.message,
                isLoading = false,
                isRefreshing = false,
                isLoadingMore = false,
              )
            }
            delay(3000)
            _state.update {
              it.copy(
                alert = null
              )
            }

          }

          is Result.Success -> {

            // TODO: REMOVE
            delay(1000)
            _state.update {
              val currentData = it.data
              val incomingData = result.data
              val existingIds =
                currentData?.results?.map { movie -> movie.id }?.toSet() ?: emptySet()
              val filteredNewResults =
                incomingData.results.filterNot { movie -> movie.id in existingIds }

              val newData = currentData?.copy(
                results = currentData.results + filteredNewResults
              ) ?: incomingData.copy(results = filteredNewResults)


              it.copy(
                isLoading = false,
                isLoadingMore = false,
                data = newData,
                error = null,
                isRefreshing = false
              )
            }
            _state.update {
              it.copy(
                page = it.page + 1
              )
            }
          }

          is Result.Error   -> {
            _state.update {
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
        _state.update {
          it.copy(
            isLoading = false,
            isRefreshing = false,
            isLoadingMore = false,
            error = ErrorState(message = exception.message),
            page = 1,

            )
        }
      }
      // ! semua perubahan state dijalankan di main thread + memastikan aliran data aware terhadap lifecycle
      .launchIn(viewModelScope)
  }

  fun handleLoading() {
    if (! _state.value.isRefreshing) {
      when (isStillOnFirstPage) {
        true  -> {
          _state.update {
            it.copy(
              isLoading = true,
              error = null
            )
          }
        }

        false -> {
          _state.update {
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
  val data : PaginationMovie? = null,
  val previousData : PaginationMovie? = null
)

sealed class HomeEvent {
  object OnLoad : HomeEvent()
  object OnRefresh : HomeEvent()
  object OnAlertDismissed : HomeEvent()
  object OnAlertActive : HomeEvent()
}
