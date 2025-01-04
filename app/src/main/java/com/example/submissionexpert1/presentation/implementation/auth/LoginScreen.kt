package com.example.submissionexpert1.presentation.implementation.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
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
import com.example.submissionexpert1.presentation.viewmodel.auth.LoginEvent
import com.example.submissionexpert1.presentation.viewmodel.auth.LoginViewModel

@Composable
fun LoginScreen(
  modifier : Modifier,
  onNavigateRegister : () -> Unit,
  onSuccessfulLogin : () -> Unit,
  viewModel : LoginViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()
  val context = LocalContext.current

  LaunchedEffect(state.message) {
    state.message?.let {
      if (it is Message.Success) {
        onSuccessfulLogin()
      } else {
        Toast.makeText(context, (it as Message.Error).message, Toast.LENGTH_SHORT).show()
      }
    }
  }

  Box(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .padding(bottom = 32.dp)
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      TextFieldsLogin(
        email = state.email,
        password = state.password,
        isPasswordVisible = state.isPasswordVisible,
        onEmailChange = {
          viewModel.onEvent(LoginEvent.OnEmailChange(it))
        },
        onPasswordChange = {
          viewModel.onEvent(LoginEvent.OnPasswordChange(it))
        },
        onPasswordToggle = {
          viewModel.onEvent(LoginEvent.TogglePasswordVisibility)
        },
        emailError = state.emailError,
        passwordError = state.passwordError
      )
      Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
          MainText(
            text = "Register",
            onClick = { onNavigateRegister() },
            textSize = Size.Small
          )
        }
        MainButton(
          text = if (state.isLoading) "Loading..." else "Login",
          onClick = {
            viewModel.onEvent(LoginEvent.OnLogin)
          },
          modifier = Modifier.fillMaxWidth(),
          isEnabled = ! state.isLoading
        )
      }
    }
  }
}

@Composable
private fun TextFieldsLogin(
  email : String,
  password : String,
  isPasswordVisible : Boolean,
  onEmailChange : (String) -> Unit,
  onPasswordChange : (String) -> Unit,
  onPasswordToggle : () -> Unit,
  emailError : String?,
  passwordError : String?
) {
  MainTextField(
    value = email,
    onValueChange = {
      onEmailChange(it)
    },
    label = "Email",
    placeholder = "Email here",
    keyboardOptions = KeyboardOptions.Default.copy(
      keyboardType = KeyboardType.Email,
    ),
    error = emailError
  )
  MainTextField(
    value = password,
    onValueChange = {
      onPasswordChange(it)
    },
    label = "Password",
    placeholder = "Password here",
    keyboardOptions = KeyboardOptions.Default.copy(
      keyboardType = KeyboardType.Password
    ),
    error = passwordError,
    isPasswordVisible = isPasswordVisible,
    trailingIcon = {
      IconToggleButton(
        checked = isPasswordVisible,
        onCheckedChange = {
          onPasswordToggle()
        }
      ) {
        if (isPasswordVisible) {
          Icon(
            imageVector = Icons.Filled.Visibility,
            contentDescription = "Hide password"
          )
        } else {
          Icon(
            imageVector = Icons.Filled.VisibilityOff,
            contentDescription = "Show password"
          )
        }
      }
    }
  )
}
