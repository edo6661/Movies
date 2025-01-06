package com.example.submissionexpert1.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import com.example.submissionexpert1.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
  private val userPreferences : UserPreferences,
) : ViewModel() {


  private val _state = MutableStateFlow(MainState())
  val state get() = _state

  init {
    getUser()
  }

  fun onEvent(event : MainEvent) {
    when (event) {
      is MainEvent.GetUser -> getUser()
      is MainEvent.Logout  -> logout()
    }
  }

  private fun getUser() {
    viewModelScope.launch {
      userPreferences.getUserData().collect { user ->
        _state.value = MainState(
          user = user,
        )
      }
    }
  }

  private fun logout() {
    viewModelScope.launch {
      userPreferences.clearUserSession()
      _state.value = MainState()
    }
  }


}

data class MainState(
  val user : User? = null,
)

sealed class MainEvent {
  data object GetUser : MainEvent()
  data object Logout : MainEvent()
}