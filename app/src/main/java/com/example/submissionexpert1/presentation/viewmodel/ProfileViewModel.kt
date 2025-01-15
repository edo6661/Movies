package com.example.submissionexpert1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cori.constants.Auth
import com.example.domain.common.Result
import com.example.domain.usecase.user.IAuthUseCase
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import com.example.submissionexpert1.presentation.common.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val userPreferences : UserPreferences,
  private val useCase : IAuthUseCase,
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher
) : ViewModel() {

  private var _uiState = MutableStateFlow(ProfileState())
  val uiState = _uiState.asStateFlow()


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

  fun onEvent(event : ProfileEvent) {
    when (event) {
      is ProfileEvent.UpdateUser                    -> updateUser()

      is ProfileEvent.OnChangeFirstName             -> {
        _uiState.value = _uiState.value.copy(
          user = _uiState.value.user?.copy(firstName = event.firstName)
        )
      }

      is ProfileEvent.OnChangeLastName              -> {
        _uiState.value = _uiState.value.copy(
          user = _uiState.value.user?.copy(lastName = event.lastName)
        )
      }

      is ProfileEvent.OnChangeEmail                 -> {
        _uiState.value = _uiState.value.copy(
          user = _uiState.value.user?.copy(email = event.email)
        )
      }

      is ProfileEvent.OnChangePassword              -> {
        _uiState.value = _uiState.value.copy(
          user = _uiState.value.user?.copy(password = event.password)
        )
      }

      is ProfileEvent.OnChangeNewPassword           -> {
        _uiState.value = _uiState.value.copy(
          newPassword = event.newPassword
        )
      }

      is ProfileEvent.OnChangePasswordVisibility    -> {
        _uiState.value = _uiState.value.copy(
          isPasswordVisible = event.isVisible
        )
      }

      is ProfileEvent.OnChangeNewPasswordVisibility -> {
        _uiState.value = _uiState.value.copy(
          isNewPasswordVisible = event.isVisible
        )
      }

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

  private fun validateForm(user : com.example.domain.model.User) : Boolean {
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


sealed class ProfileEvent {
  data object UpdateUser : ProfileEvent()
  data class OnChangeFirstName(val firstName : String) : ProfileEvent()
  data class OnChangeLastName(val lastName : String) : ProfileEvent()
  data class OnChangeEmail(val email : String) : ProfileEvent()
  data class OnChangePassword(val password : String) : ProfileEvent()
  data class OnChangeNewPassword(val newPassword : String) : ProfileEvent()
  data class OnChangePasswordVisibility(val isVisible : Boolean) : ProfileEvent()
  data class OnChangeNewPasswordVisibility(val isVisible : Boolean) : ProfileEvent()
}

data class ProfileState(
  val user : com.example.domain.model.User? = null,
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