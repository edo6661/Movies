package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import com.example.submissionexpert1.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingViewModel @Inject constructor(
  private val userPreferences : UserPreferences,
) : ViewModel() {

  private var _user : User? = null
  val user : User?
    get() = _user

  init {
    viewModelScope.launch {
      userPreferences.getUserData().first()?.let {
        _user = it
      }
    }
  }

  private fun logout() {
    viewModelScope.launch {
      userPreferences.clearUserSession()
    }
  }

  fun onEvent(event : SettingEvent) {
    when (event) {
      SettingEvent.Logout -> logout()
    }
  }
}

sealed class SettingEvent {
  object Logout : SettingEvent()
}