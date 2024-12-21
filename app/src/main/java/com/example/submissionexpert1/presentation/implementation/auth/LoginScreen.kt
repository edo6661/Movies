package com.example.submissionexpert1.presentation.implementation.auth

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.presentation.ui.shared.MainButton
import com.example.submissionexpert1.presentation.ui.shared.MainTextField

@Composable
fun LoginScreen(
  modifier : Modifier,
  onNavigateRegister : () -> Unit
) {
  var email by remember { mutableStateOf("") }
  var emailError : String? by remember { mutableStateOf(null) }
  var password by remember { mutableStateOf("") }
  var passwordError : String? by remember { mutableStateOf(null) }
  var isPasswordVisible by remember { mutableStateOf(false) }

  val onSubmit = {
    when {
      email.isEmpty() && password.isEmpty() -> {
        emailError = "Email must not be empty"
        passwordError = "Password must not be empty"
      }

      email.isEmpty()                       -> {
        emailError = "Email must not be empty"
      }

      password.isEmpty()                    -> {
        passwordError = "Password must not be empty"
      }

      else                                  -> {
        emailError = null
        passwordError = null
        Log.d("LoginScreen", "Email : $email, Password : $password")
      }
    }
  }

  Box(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .padding(bottom = 32.dp)
  ) {
    Column {
      TextFieldsLogin(
        email = email,
        password = password,
        isPasswordVisible = isPasswordVisible,
        onEmailChange = {
          email = it
        },
        onPasswordChange = {
          password = it
        },
        onPasswordToggle = {
          isPasswordVisible = ! isPasswordVisible
        },
        emailError = emailError,
        passwordError = passwordError

      )
      MainButton(
        text = "Login",
        onClick = {
          onSubmit()
        },
        modifier = Modifier.fillMaxWidth()
      )

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
    // TODO: NANTI GANTI, PAKE VIEW MODEL
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