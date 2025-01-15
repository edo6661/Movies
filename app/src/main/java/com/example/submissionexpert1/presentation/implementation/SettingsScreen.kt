package com.example.submissionexpert1.presentation.implementation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.shared.MainText
import com.example.submissionexpert1.presentation.viewmodel.SettingEvent
import com.example.submissionexpert1.presentation.viewmodel.SettingViewModel

@Composable
fun SettingsScreen(
  modifier : Modifier,
  navigateToProfile : () -> Unit,
  navigateToTheme : () -> Unit,
  vm: SettingViewModel = hiltViewModel(),
  onNavigateLogout: () -> Unit
) {
  val onLogout = {
    vm.onEvent(SettingEvent.Logout)
    onNavigateLogout()
  }
  Column(
    modifier = modifier,

    ) {
    ActionSetting(
      action = navigateToProfile,
      text = "Profile",
      icon = Icons.Default.Person,
      subtitle = "Manage your personal information"
    )
    HorizontalDivider(
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    )

    ActionSetting(
      action = navigateToTheme,
      text = "Theme",
      icon = Icons.Default.ColorLens,
      subtitle = "Customize app appearance"
    )
    HorizontalDivider(
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    )


    ActionSetting(
      action = onLogout,
      text = "Logout",
      icon = Icons.AutoMirrored.Default.Logout,
      subtitle = "Sign out from your account"
    )


  }

}
@Composable
private fun ActionSetting(
  modifier: Modifier = Modifier,
  action: () -> Unit,
  text: String,
  icon: ImageVector,
  subtitle: String? = null,
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clickable(
        indication = rememberRipple(bounded = true),
        interactionSource = remember { MutableInteractionSource() }
      ) { action() },
    color = Color.Transparent
  ) {
    Row(
      modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .height(IntrinsicSize.Min),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(24.dp),
        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
      )

      Column(modifier = Modifier.weight(1f)) {
        MainText(
          text = text,
          textSize = Size.Large
        )
        if (subtitle != null) {
          MainText(
            text = subtitle,
            textSize = Size.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
          )
        }
      }

      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = null,
        modifier = Modifier.size(20.dp),
        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
      )
    }
  }
}

