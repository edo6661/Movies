package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.User
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingViewModel @Inject constructor(
  private val userPreferences : UserPreferences,
) : ViewModel() {

  private var _user : MutableStateFlow<User?> = MutableStateFlow(null)
  val user = _user.asStateFlow()

  init {
    viewModelScope.launch {
      userPreferences.getUserData().collect {
        _user.value = it
      }
    }
  }

  private fun logout() {
    viewModelScope.launch {
      userPreferences.clearUserSession()
      _user.value = null
    }
  }

  fun onEvent(event : SettingEvent) {
    when (event) {
      SettingEvent.Logout -> logout()
    }
  }
}

sealed class SettingEvent {
  data object Logout : SettingEvent()
}