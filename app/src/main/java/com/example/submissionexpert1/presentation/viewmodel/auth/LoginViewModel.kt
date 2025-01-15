package com.example.submissionexpert1.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.core.extensions.validateEmail
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.usecase.user.IAuthUseCase
import com.example.submissionexpert1.presentation.common.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val useCase : IAuthUseCase,
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher
) : ViewModel() {

  private val _state = MutableStateFlow(LoginState())
  val state : StateFlow<LoginState> get() = _state

  fun onEvent(event : LoginEvent) {
    when (event) {
      is LoginEvent.OnEmailChange            -> updateState { copy(email = event.email) }
      is LoginEvent.OnPasswordChange         -> updateState { copy(password = event.password) }
      is LoginEvent.TogglePasswordVisibility -> updateState { copy(isPasswordVisible = ! isPasswordVisible) }
      is LoginEvent.OnLogin                  -> validateAndLogin()
    }
  }

  private fun updateState(update : LoginState.() -> LoginState) {
    _state.value = _state.value.update()
  }

  private fun resetErrors() {
    _state.value = _state.value.copy(
      emailError = null,
      passwordError = null
    )
  }

  private fun validateAndLogin() {
    val currentState = _state.value

    val emailError = if (currentState.email.validateEmail()) null else "Invalid Email"
    val passwordError = if (currentState.password.isEmpty()) "Password must not be empty" else null

    if (emailError == null && passwordError == null) {
      resetErrors()
      performLogin()
    } else {
      _state.value = currentState.copy(
        emailError = emailError,
        passwordError = passwordError
      )
    }
  }

  private fun performLogin() {
    val currentState = _state.value

    updateState { copy(isLoading = true) }
    viewModelScope.launch(ioDispatcher) {
      useCase.login(currentState.email, currentState.password)
        .onStart {
          updateState {
            copy(
              isLoading = true,
            )
          }
        }
        .collect { result ->
          withContext(mainDispatcher) {
            when (result) {
              is Result.Success -> {
                updateState { copy(isLoading = false, message = Message.Success(result.data)) }
              }

              is Result.Error   -> {
                updateState {
                  copy(
                    isLoading = false,
                    message = Message.Error(result.message),
                    password = ""
                  )
                }
              }
            }
          }
        }
    }
  }
}

sealed class LoginEvent {
  data class OnEmailChange(val email : String) : LoginEvent()
  data class OnPasswordChange(val password : String) : LoginEvent()
  data object TogglePasswordVisibility : LoginEvent()
  data object OnLogin : LoginEvent()
}

data class LoginState(
  val email : String = "",
  val password : String = "",
  val message : Message? = null,
  val isPasswordVisible : Boolean = false,
  val emailError : String? = null,
  val passwordError : String? = null,
  val isLoading : Boolean = false
)
