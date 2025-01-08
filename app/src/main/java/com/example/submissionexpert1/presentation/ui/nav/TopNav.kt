package com.example.submissionexpert1.presentation.ui.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.domain.model.User
import com.example.submissionexpert1.presentation.ui.shared.MainSearchBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun TopNav(
  currentRoute : String?,
  user : User? = null,
  logout : () -> Unit,
  navigateToSearch : () -> Unit,
) {
  TopAppBar(
    windowInsets = WindowInsets.systemBars,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        MainSearchBar(
          query = "",
          onQueryChange = {
          },
          onSearch = {
          },
          active = false,
          onActiveChange = {
            navigateToSearch()
          },
          enabled = true,
          modifier = Modifier.weight(1f),
          allowKeyboard = false,

          )
        if (user != null) {
          IconButton(
            onClick = logout,
            modifier = Modifier
              .padding(end = 16.dp)
              .background(MaterialTheme.colorScheme.primary, CircleShape)
          ) {
            Icon(
              contentDescription = "Logout",
              imageVector = Icons.AutoMirrored.Filled.Logout,
              tint = MaterialTheme.colorScheme.onPrimary
            )
          }
        }
      }
    },
    actions = {}
  )
}
