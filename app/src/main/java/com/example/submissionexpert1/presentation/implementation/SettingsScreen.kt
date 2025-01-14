package com.example.submissionexpert1.presentation.implementation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
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
import com.example.submissionexpert1.data.source.local.preferences.ThemePreferences
import com.example.submissionexpert1.presentation.common.Message
import com.example.submissionexpert1.presentation.ui.shared.MainButton
import com.example.submissionexpert1.presentation.ui.shared.MainTextField
import com.example.submissionexpert1.presentation.viewmodel.SettingEvent
import com.example.submissionexpert1.presentation.viewmodel.SettingState
import com.example.submissionexpert1.presentation.viewmodel.SettingViewModel

@Composable
fun SettingsScreen(
  modifier : Modifier,
  vm : SettingViewModel = hiltViewModel(),
  onNavigateLogin : () -> Unit
) {
  val theme by vm.themeMode.collectAsState()
  val state by vm.uiState.collectAsState()
  val onEvent : (SettingEvent) -> Unit = { vm.onEvent(it) }
  val context = LocalContext.current

  val onSuccessfulLogin : (String) -> Unit = {
    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    onNavigateLogin()

  }
  LaunchedEffect(state.message) {
    state.message?.let {
      if (it is Message.Success) {
        onSuccessfulLogin(it.message)
      } else {
        Toast.makeText(context, (it as Message.Error).message, Toast.LENGTH_SHORT).show()
      }
    }
  }
  Column(
    modifier = modifier,
  ) {

    ThemeOption(
      title = "Light Mode",
      selected = ThemePreferences.THEME_LIGHT == theme,
      onClick = { onEvent(SettingEvent.SetThemeMode(ThemePreferences.THEME_LIGHT)) }
    )

    ThemeOption(
      title = "Dark Mode",
      selected = ThemePreferences.THEME_DARK == theme,
      onClick = { onEvent(SettingEvent.SetThemeMode(ThemePreferences.THEME_DARK)) }
    )

    ThemeOption(
      title = "Follow System",
      selected = ThemePreferences.THEME_SYSTEM == theme,
      onClick = { onEvent(SettingEvent.SetThemeMode(ThemePreferences.THEME_SYSTEM)) }
    )
    if (state.user != null) {
      HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp)
      )
      Spacer(
        modifier = Modifier.height(16.dp)
      )
      TextFieldsSetting(
        state = state,
        onEvent = onEvent
      )
    }


  }
}

@Composable
fun ThemeOption(
  title : String,
  selected : Boolean,
  onClick : () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick),
    verticalAlignment = Alignment.CenterVertically
  ) {
    RadioButton(
      selected = selected,
      onClick = onClick
    )
    Text(text = title)
  }
}


@Suppress("t")
@Composable
fun TextFieldsSetting(
  state : SettingState,
  onEvent : (SettingEvent) -> Unit
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.padding(horizontal = 16.dp)
  ) {
    MainTextField(
      value = state.user?.firstName ?: "",
      onValueChange = {
        onEvent(SettingEvent.OnChangeFirstName(it))
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
        onEvent(SettingEvent.OnChangeLastName(it))
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
        onEvent(SettingEvent.OnChangeEmail(it))
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
        onEvent(SettingEvent.OnChangePassword(it))
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
            onEvent(SettingEvent.OnChangePasswordVisibility(! state.isPasswordVisible))
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
        onEvent(SettingEvent.OnChangeNewPassword(it))
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
            onEvent(SettingEvent.OnChangeNewPasswordVisibility(! state.isNewPasswordVisible))
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
      onClick = { onEvent(SettingEvent.UpdateUser) },
      modifier = Modifier.fillMaxWidth(),
      isEnabled = ! state.isLoading
    )
  }
}