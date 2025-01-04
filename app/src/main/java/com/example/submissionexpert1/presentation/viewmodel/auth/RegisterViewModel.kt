package com.example.submissionexpert1.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionexpert1.application.di.IODispatcher
import com.example.submissionexpert1.application.di.MainDispatcher
import com.example.submissionexpert1.core.extensions.validate3Char
import com.example.submissionexpert1.core.extensions.validateConfirmPassword
import com.example.submissionexpert1.core.extensions.validateEmail
import com.example.submissionexpert1.core.utils.hashPassword
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.User
import com.example.submissionexpert1.domain.usecase.user.IAuthUseCase
import com.example.submissionexpert1.presentation.common.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
  private val useCase : IAuthUseCase,
  @IODispatcher private val ioDispatcher : CoroutineDispatcher,
  @MainDispatcher private val mainDispatcher : CoroutineDispatcher

) : ViewModel() {

  private val _state = MutableStateFlow(RegisterState())
  val state : StateFlow<RegisterState> get() = _state

  fun onEvent(event : RegisterEvent) {
    when (event) {
      is RegisterEvent.OnFirstNameChange               -> updateState { copy(firstName = event.firstName) }
      is RegisterEvent.OnLastNameChange                -> updateState { copy(lastName = event.lastName) }
      is RegisterEvent.OnEmailChange                   -> updateState { copy(email = event.email) }
      is RegisterEvent.OnPasswordChange                -> updateState { copy(password = event.password) }
      is RegisterEvent.OnConfirmPasswordChange         -> updateState { copy(confirmPassword = event.confirmPassword) }
      is RegisterEvent.TogglePasswordVisibility        -> updateState { copy(isPasswordVisible = ! isPasswordVisible) }
      is RegisterEvent.ToggleConfirmPasswordVisibility -> updateState {
        copy(
          isConfirmPasswordVisible = ! isConfirmPasswordVisible
        )
      }

      is RegisterEvent.OnRegister                      -> validateAndRegister()
    }
  }

  private fun updateState(update : RegisterState.() -> RegisterState) {
    _state.value = _state.value.update()
  }

  private fun resetError() {
    _state.value = _state.value.copy(
      firstNameError = null,
      lastNameError = null,
      emailError = null,
      passwordError = null,
      confirmPasswordError = null
    )
  }

  private fun resetState() {
    _state.value = RegisterState()
  }


  private fun validateAndRegister() {
    val currentState = _state.value

    val firstNameError =
      if (currentState.firstName.validate3Char()) null else "First Name must have at least 3 characters"
    val lastNameError =
      if (currentState.lastName.validate3Char()) null else "Last Name must have at least 3 characters"
    val emailError = if (currentState.email.validateEmail()) null else "Invalid email format"
    val passwordError =
      if (currentState.password.validate3Char()) null else "Password must have at least 3 characters"
    val confirmPasswordError =
      if (currentState.confirmPassword.validateConfirmPassword(currentState.password)) null else "Passwords do not match"

    if (listOf(
        firstNameError,
        lastNameError,
        emailError,
        passwordError,
        confirmPasswordError
      ).all { it == null }
    ) {
      resetError()
      performRegistration()
    } else {
      _state.value = currentState.copy(
        firstNameError = firstNameError,
        lastNameError = lastNameError,
        emailError = emailError,
        passwordError = passwordError,
        confirmPasswordError = confirmPasswordError
      )
    }
  }

  private fun performRegistration() {
    val hashedPassword = hashPassword(_state.value.password)
    updateState { copy(isLoading = true) }
    viewModelScope.launch(ioDispatcher) {
      useCase.register(
        User(
          firstName = _state.value.firstName,
          lastName = _state.value.lastName,
          email = _state.value.email,
          password = hashedPassword
        )
      ).collect { result ->
        withContext(mainDispatcher) {

          when (result) {
            is Result.Success -> {

              updateState { copy(isLoading = false, message = Message.Success(result.data)) }
            }

            is Result.Error   -> {
              if (result.message == "Email Already Exist") {
                updateState { copy(isLoading = false, emailError = "Email Already Exist") }

              }
              updateState { copy(isLoading = false, message = Message.Error(result.message)) }
            }

            is Result.Loading -> {
              updateState { copy(isLoading = true) }
            }
          }
        }
      }


    }
  }
}

sealed class RegisterEvent {
  data class OnFirstNameChange(val firstName : String) : RegisterEvent()
  data class OnLastNameChange(val lastName : String) : RegisterEvent()
  data class OnEmailChange(val email : String) : RegisterEvent()
  data class OnPasswordChange(val password : String) : RegisterEvent()
  data class OnConfirmPasswordChange(val confirmPassword : String) : RegisterEvent()
  data object TogglePasswordVisibility : RegisterEvent()
  data object ToggleConfirmPasswordVisibility : RegisterEvent()
  data object OnRegister : RegisterEvent()
}


data class RegisterState(
  val firstName : String = "",
  val lastName : String = "",
  val email : String = "",
  val password : String = "",
  val confirmPassword : String = "",
  val isPasswordVisible : Boolean = false,
  val isConfirmPasswordVisible : Boolean = false,
  val firstNameError : String? = null,
  val lastNameError : String? = null,
  val emailError : String? = null,
  val passwordError : String? = null,
  val confirmPasswordError : String? = null,
  val message : Message? = null,
  val isLoading : Boolean = false,

  )

