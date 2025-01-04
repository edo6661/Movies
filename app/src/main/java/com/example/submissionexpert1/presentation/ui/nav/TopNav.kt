package com.example.submissionexpert1.presentation.ui.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.R
import com.example.submissionexpert1.domain.model.User
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.shared.MainText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun TopNav(
  currentRoute : String?,
  user : User? = null,
  logout : () -> Unit
) {
  TopAppBar(
    title = {
      Text(text = currentRoute ?: R.string.app_name.toString())
    },
    actions = {
      if (user != null) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(
            12.dp
          ),

          ) {
          MainText(
            text = user.firstName,
            textSize = Size.Small
          )
          Icon(
            contentDescription = "Logout",
            imageVector = Icons.AutoMirrored.Filled.Logout,
            modifier = Modifier.clickable { logout() }
          )
        }
      }
    }
  )

}

