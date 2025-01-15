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
class SettingViewModel @Inject constructor(
  private val themePreferences : ThemePreferences,
  private val userPreferences : UserPreferences,
  private val useCase : IAuthUseCase,
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher
) : ViewModel() {

  var _uiState = MutableStateFlow(SettingState())
  val uiState = _uiState.asStateFlow()

  val themeMode = themePreferences.getThemeMode()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemePreferences.THEME_SYSTEM)

  init {
    initializeUser()
  }

  private fun initializeUser() {
    viewModelScope.launch {
      userPreferences.getUserData().collect { user ->
        user?.let {
          _uiState.value = _uiState.value.copy(
            user = user.copy(
              password = ""
            )
          )
        } ?: Message.Error("User not found")
      }
    }
  }

  fun onEvent(event : SettingEvent) {
    when (event) {
      is SettingEvent.SetThemeMode                  -> updateThemeMode(event.themeMode)
      is SettingEvent.UpdateUser                    -> updateUser()

      is SettingEvent.OnChangeFirstName             -> {
        _uiState.value = _uiState.value.copy(
          user = _uiState.value.user?.copy(firstName = event.firstName)
        )
      }

      is SettingEvent.OnChangeLastName              -> {
        _uiState.value = _uiState.value.copy(
          user = _uiState.value.user?.copy(lastName = event.lastName)
        )
      }

      is SettingEvent.OnChangeEmail                 -> {
        _uiState.value = _uiState.value.copy(
          user = _uiState.value.user?.copy(email = event.email)
        )
      }

      is SettingEvent.OnChangePassword              -> {
        _uiState.value = _uiState.value.copy(
          user = _uiState.value.user?.copy(password = event.password)
        )
      }

      is SettingEvent.OnChangeNewPassword           -> {
        _uiState.value = _uiState.value.copy(
          newPassword = event.newPassword
        )
      }

      is SettingEvent.OnChangePasswordVisibility    -> {
        _uiState.value = _uiState.value.copy(
          isPasswordVisible = event.isVisible
        )
      }

      is SettingEvent.OnChangeNewPasswordVisibility -> {
        _uiState.value = _uiState.value.copy(
          isNewPasswordVisible = event.isVisible
        )
      }

    }

  }


  private fun updateThemeMode(themeMode : String) {
    viewModelScope.launch {
      themePreferences.setThemeMode(themeMode)
    }
  }


  private fun updateUser() {
    val user = _uiState.value.user
    if (! validateForm(user !!)) return
    viewModelScope.launch(ioDispatcher) {
      useCase.update(
        user = user,
        newPassword = _uiState.value.newPassword
      )
        .onStart {
          withContext(mainDispatcher) {
            _uiState.value = _uiState.value.copy(isLoading = true)
          }
        }
        .collect { result ->
          when (result) {
            is Result.Success -> {
              withContext(mainDispatcher) {
                _uiState.value = _uiState.value.copy(
                  isLoading = false,
                  message = result.data.let { Message.Success(it) }
                )
              }
            }

            is Result.Error   -> {
              withContext(mainDispatcher) {
                when (result.message) {
                  Auth.EMAIL_ALREADY_EXIST -> {
                    _uiState.value = _uiState.value.copy(
                      isLoading = false,
                      emailError = result.message
                    )
                  }

                  Auth.PASSWORD_INVALID    -> {
                    _uiState.value = _uiState.value.copy(
                      isLoading = false,
                      passwordError = result.message,
                      user = _uiState.value.user?.copy(password = "")
                    )
                  }

                  else                     -> {
                    _uiState.value = _uiState.value.copy(
                      isLoading = false,
                      message = Message.Error(result.message)
                    )
                  }
                }


              }


            }
          }
        }
    }
  }

  private fun resetError() {
    _uiState.value = _uiState.value.copy(
      firstNameError = null,
      lastNameError = null,
      emailError = null,
      passwordError = null,
      newPasswordError = null
    )
  }

  private fun validateForm(user : User) : Boolean {
    resetError()

    var isValid = true
    if (user.firstName.isEmpty()) {
      _uiState.value = _uiState.value.copy(
        firstNameError = "First Name must not be empty"
      )
      isValid = false
    }
    if (user.lastName.isEmpty()) {
      _uiState.value = _uiState.value.copy(
        lastNameError = "Last Name must not be empty"
      )
      isValid = false
    }
    if (user.email.isEmpty()) {
      _uiState.value = _uiState.value.copy(
        emailError = "Email must not be empty"
      )
      isValid = false
    }
    if (user.password.isEmpty()) {
      _uiState.value = _uiState.value.copy(
        passwordError = "Password must not be empty"
      )
      isValid = false
    }
    if (user.password.isEmpty()) {
      _uiState.value = _uiState.value.copy(
        newPasswordError = "New Password must not be empty"
      )
      isValid = false
    }
    if (user.password == _uiState.value.newPassword) {
      _uiState.value = _uiState.value.copy(
        newPasswordError = "New Password must not be the same as Old Password"
      )
      isValid = false
    }
    return isValid
  }

}


sealed class SettingEvent {
  data class SetThemeMode(val themeMode : String) : SettingEvent()
  data object UpdateUser : SettingEvent()
  data class OnChangeFirstName(val firstName : String) : SettingEvent()
  data class OnChangeLastName(val lastName : String) : SettingEvent()
  data class OnChangeEmail(val email : String) : SettingEvent()
  data class OnChangePassword(val password : String) : SettingEvent()
  data class OnChangeNewPassword(val newPassword : String) : SettingEvent()
  data class OnChangePasswordVisibility(val isVisible : Boolean) : SettingEvent()
  data class OnChangeNewPasswordVisibility(val isVisible : Boolean) : SettingEvent()
}

data class SettingState(
  val user : User? = null,
  val newPassword : String = "",
  val firstNameError : String? = null,
  val lastNameError : String? = null,
  val emailError : String? = null,
  val passwordError : String? = null,
  val newPasswordError : String? = null,
  val isPasswordVisible : Boolean = false,
  val isNewPasswordVisible : Boolean = false,
  val isLoading : Boolean = false,
  val message : Message? = null
)