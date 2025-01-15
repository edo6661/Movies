package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.core.constants.Auth
import com.example.submissionexpert1.data.source.local.preferences.ThemePreferences
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.User
import com.example.submissionexpert1.domain.usecase.user.IAuthUseCase
import com.example.submissionexpert1.presentation.common.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



@HiltViewModel
class ThemeViewModel @Inject constructor(
  private val themePreferences : ThemePreferences,
) : ViewModel() {


  val themeMode = themePreferences.getThemeMode()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemePreferences.THEME_SYSTEM)


  fun onEvent(event : ThemeEvent) {
    when (event) {
      is ThemeEvent.SetThemeMode -> updateThemeMode(event.themeMode)
    }
  }

  private fun updateThemeMode(themeMode : String) {
    viewModelScope.launch {
      themePreferences.setThemeMode(themeMode)
    }
  }


}


sealed class ThemeEvent {
  data class SetThemeMode(val themeMode : String) : ThemeEvent()

}
