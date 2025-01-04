package com.example.submissionexpert1.presentation.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.submissionexpert1.presentation.navigation.Screen

data class BottomBarItem(
  val label : String = "",
  val icon : ImageVector = Icons.Filled.Home,
  val route : String = ""
) {

  fun bottomBarItems(
    isUserLoggedIn : Boolean = false
  ) : List<BottomBarItem> {
    val items = mutableListOf(
      BottomBarItem(
        label = Screen.Home.route,
        icon = Icons.Filled.Home,
        route = Screen.Home.route
      ),
      BottomBarItem(
        label = Screen.Favorite.route,
        icon = Icons.Filled.Favorite,
        route = Screen.Favorite.route
      )
    )

    if (! isUserLoggedIn) {
      items.add(
        BottomBarItem(
          label = Screen.Auth.Login.route,
          icon = Icons.AutoMirrored.Default.Login,
          route = Screen.Auth.Login.route
        )
      )
    }

    return items
  }
}
