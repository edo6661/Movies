package com.example.submissionexpert1.presentation.navigation

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.presentation.common.Message
import com.example.submissionexpert1.presentation.ui.shared.MainButton
import com.example.submissionexpert1.presentation.ui.shared.MainTextField
import com.example.submissionexpert1.presentation.viewmodel.ProfileEvent
import com.example.submissionexpert1.presentation.viewmodel.ProfileState
import com.example.submissionexpert1.presentation.viewmodel.ProfileViewModel


@Composable
fun ProfileScreen(
  modifier : Modifier,
  vm : ProfileViewModel = hiltViewModel(),
  onNavigateLogin : () -> Unit
) {
  val state by vm.uiState.collectAsState()
  val onEvent : (ProfileEvent) -> Unit = { vm.onEvent(it) }
  val context = LocalContext.current

  val onSuccessUpdate : (String) -> Unit = {
    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    onNavigateLogin()

  }
  LaunchedEffect(state.message) {
    state.message?.let {
      if (it is Message.Success) {
        onSuccessUpdate(it.message)
      } else {
        Toast.makeText(context, (it as Message.Error).message, Toast.LENGTH_SHORT).show()
      }
    }
  }
  Column(
    modifier = modifier,
  ) {


    Spacer(
      modifier = Modifier.height(16.dp)
    )
    TextFieldsSetting(
      state = state,
      onEvent = onEvent
    )


  }
}


@Suppress("t")
@Composable
fun TextFieldsSetting(
  state : ProfileState,
  onEvent : (ProfileEvent) -> Unit
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.padding(horizontal = 16.dp)
  ) {
    MainTextField(
      value = state.user?.firstName ?: "",
      onValueChange = {
        onEvent(ProfileEvent.OnChangeFirstName(it))
      },
      label = "First Name",
      placeholder = "First Name here",
      error = state.firstNameError,
      unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
      focusedContainerColor = MaterialTheme.colorScheme.tertiary,


      )
    MainTextField(
      value = state.user?.lastName ?: "",
      onValueChange = {
        onEvent(ProfileEvent.OnChangeLastName(it))
      },
      label = "Last Name",
      placeholder = "Last Name here",
      error = state.lastNameError,
      unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
      focusedContainerColor = MaterialTheme.colorScheme.tertiary,


      )
    MainTextField(
      value = state.user?.email ?: "",
      onValueChange = {
        onEvent(ProfileEvent.OnChangeEmail(it))
      },
      label = "Email",
      placeholder = "Email here",
      keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
      error = state.emailError,
      unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
      focusedContainerColor = MaterialTheme.colorScheme.tertiary,


      )
    MainTextField(
      value = state.user?.password ?: "",
      onValueChange = {
        onEvent(ProfileEvent.OnChangePassword(it))
      },
      label = "Old Password",
      placeholder = "Old Password here",
      keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
      error = state.passwordError,
      isPasswordVisible = state.isPasswordVisible,
      unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
      focusedContainerColor = MaterialTheme.colorScheme.tertiary,


      trailingIcon = {
        IconToggleButton(
          checked = state.isPasswordVisible,
          onCheckedChange = {
            onEvent(ProfileEvent.OnChangePasswordVisibility(! state.isPasswordVisible))
          }
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
      value = state.newPassword,
      onValueChange = {
        onEvent(ProfileEvent.OnChangeNewPassword(it))
      },
      label = "New Password",
      placeholder = "New Password here",
      keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
      error = state.newPasswordError,
      isPasswordVisible = state.isNewPasswordVisible,
      unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
      focusedContainerColor = MaterialTheme.colorScheme.tertiary,


      trailingIcon = {
        IconToggleButton(
          checked = state.isNewPasswordVisible,
          onCheckedChange = {
            onEvent(ProfileEvent.OnChangeNewPasswordVisibility(! state.isNewPasswordVisible))
          }
        ) {
          Icon(
            imageVector = if (state.isNewPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
            contentDescription = if (state.isNewPasswordVisible) "Hide password" else "Show password",
            tint = MaterialTheme.colorScheme.onSecondary
          )
        }
      }
    )
    MainButton(
      text = if (state.isLoading) "Loading..." else "Update",
      onClick = { onEvent(ProfileEvent.UpdateUser) },
      modifier = Modifier.fillMaxWidth(),
      isEnabled = ! state.isLoading
    )
  }
}