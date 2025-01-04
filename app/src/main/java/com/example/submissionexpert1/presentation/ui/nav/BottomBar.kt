package com.example.submissionexpert1.presentation.ui.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.submissionexpert1.domain.model.User
import com.example.submissionexpert1.presentation.navigation.navigateSingleTop

@Composable
fun BottomBar(
  isActive : (String) -> Boolean,
  navController : NavHostController,
  user : User? = null,
) {
  NavigationBar {
    BottomBarItem().bottomBarItems(isUserLoggedIn = user != null)
      .forEachIndexed { _, navigationItem ->
        NavigationBarItem(
          selected = isActive(navigationItem.route),
          label = {
            Text(navigationItem.label)
          },
          icon = {
            Icon(
              navigationItem.icon,
              contentDescription = navigationItem.label
            )
          },
          onClick = {
            navController.navigateSingleTop(navigationItem.route)
          },
          alwaysShowLabel = false,
          enabled = ! isActive(navigationItem.route),
        )
      }

  }
}