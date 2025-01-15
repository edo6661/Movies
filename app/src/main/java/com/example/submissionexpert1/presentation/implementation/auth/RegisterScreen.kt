package com.example.submissionexpert1.presentation.implementation.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.presentation.common.Message
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.shared.MainButton
import com.example.submissionexpert1.presentation.ui.shared.MainText
import com.example.submissionexpert1.presentation.ui.shared.MainTextField
import com.example.submissionexpert1.presentation.viewmodel.auth.RegisterEvent
import com.example.submissionexpert1.presentation.viewmodel.auth.RegisterState
import com.example.submissionexpert1.presentation.viewmodel.auth.RegisterViewModel

@Composable
fun RegisterScreen(
  viewModel : RegisterViewModel = hiltViewModel(),
  modifier : Modifier = Modifier,
  onNavigateLogin : () -> Unit
) {
  val state by viewModel.state.collectAsState()
  val context = LocalContext.current


  LaunchedEffect(state.message) {
    state.message?.let {
      if (it is Message.Success) {
        onNavigateLogin()
      } else {
        Toast.makeText(context, (it as Message.Error).message, Toast.LENGTH_SHORT).show()
      }


    }
  }

  Box(modifier = modifier.padding(horizontal = 16.dp)) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
      TextFieldsRegister(
        state = state,
        onFirstNameChange = { viewModel.onEvent(RegisterEvent.OnFirstNameChange(it)) },
        onLastNameChange = { viewModel.onEvent(RegisterEvent.OnLastNameChange(it)) },
        onEmailChange = { viewModel.onEvent(RegisterEvent.OnEmailChange(it)) },
        onPasswordChange = { viewModel.onEvent(RegisterEvent.OnPasswordChange(it)) },
        onConfirmPasswordChange = { viewModel.onEvent(RegisterEvent.OnConfirmPasswordChange(it)) },
        onPasswordToggle = { viewModel.onEvent(RegisterEvent.TogglePasswordVisibility) },
        onConfirmPasswordToggle = { viewModel.onEvent(RegisterEvent.ToggleConfirmPasswordVisibility) }
      )

      Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
        MainText(text = "Login", onClick = onNavigateLogin, textSize = Size.Medium)
      }
      MainButton(
        text = if (state.isLoading) "Loading..." else "Register",
        onClick = { viewModel.onEvent(RegisterEvent.OnRegister) },
        modifier = Modifier.fillMaxWidth(),
        isEnabled = ! state.isLoading
      )
    }
  }
}

@Suppress("t")
@Composable
private fun TextFieldsRegister(
  state : RegisterState,
  onFirstNameChange : (String) -> Unit,
  onLastNameChange : (String) -> Unit,
  onEmailChange : (String) -> Unit,
  onPasswordChange : (String) -> Unit,
  onConfirmPasswordChange : (String) -> Unit,
  onPasswordToggle : () -> Unit,
  onConfirmPasswordToggle : () -> Unit
) {
  MainTextField(
    value = state.firstName,
    onValueChange = onFirstNameChange,
    label = "First Name",
    placeholder = "First Name here",
    error = state.firstNameError,
    unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
    focusedContainerColor = MaterialTheme.colorScheme.tertiary,

    )
  MainTextField(
    value = state.lastName,
    onValueChange = onLastNameChange,
    label = "Last Name",
    placeholder = "Last Name here",
    error = state.lastNameError,
    unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
    focusedContainerColor = MaterialTheme.colorScheme.tertiary,

    )
  MainTextField(
    value = state.email,
    onValueChange = onEmailChange,
    label = "Email",
    placeholder = "Email here",
    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
    error = state.emailError,
    unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
    focusedContainerColor = MaterialTheme.colorScheme.tertiary,

    )
  MainTextField(
    value = state.password,
    onValueChange = onPasswordChange,
    label = "Password",
    placeholder = "Password here",
    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
    error = state.passwordError,
    isPasswordVisible = state.isPasswordVisible,
    unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
    focusedContainerColor = MaterialTheme.colorScheme.tertiary,

    trailingIcon = {
      IconToggleButton(
        checked = state.isPasswordVisible,
        onCheckedChange = { onPasswordToggle() }
      ) {
        Icon(
          imageVector = if (state.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
          contentDescription = if (state.isPasswordVisible) "Hide password" else "Show password",
          tint = MaterialTheme.colorScheme.onSecondary
          
        )
      }
    }
  )
  MainTextField(
    value = state.confirmPassword,
    onValueChange = onConfirmPasswordChange,
    label = "Confirm Password",
    placeholder = "Confirm Password here",
    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
    error = state.confirmPasswordError,
    isPasswordVisible = state.isConfirmPasswordVisible,
    unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
    focusedContainerColor = MaterialTheme.colorScheme.tertiary,

    trailingIcon = {
      IconToggleButton(
        checked = state.isConfirmPasswordVisible,
        onCheckedChange = { onConfirmPasswordToggle() }
      ) {
        Icon(
          imageVector = if (state.isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
          contentDescription = if (state.isConfirmPasswordVisible) "Hide password" else "Show password",
          tint = MaterialTheme.colorScheme.onSecondary
        )
      }
    }
  )
}
