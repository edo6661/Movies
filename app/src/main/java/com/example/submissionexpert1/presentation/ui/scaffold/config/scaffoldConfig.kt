package com.example.submissionexpert1.presentation.ui.scaffold.config

import com.example.submissionexpert1.presentation.navigation.Screen

data class ScaffoldConfig(
  val showMainNav : Boolean = true,
  val showBackNav : Boolean = false,
  val showBottomBar : Boolean = true
)

fun scaffoldConfig(currentRoute : String?)
  : ScaffoldConfig {
  return when (currentRoute) {
    "Detail/{id}"        -> ScaffoldConfig(
      showMainNav = false,
      showBackNav = false,
      showBottomBar = false
    )

   
    Screen.Search.route  -> ScaffoldConfig(
      showMainNav = false,
      showBackNav = true,
      showBottomBar = false
    )

    Screen.Profile.route -> ScaffoldConfig(
      showMainNav = false,
      showBackNav = true,
      showBottomBar = false
    )

    Screen.Theme.route   -> ScaffoldConfig(
      showMainNav = false,
      showBackNav = true,
      showBottomBar = false
    )

    Screen.Home.route    -> ScaffoldConfig(
      showMainNav = false,
      showBackNav = false,
      showBottomBar = true
    )

    else                 -> ScaffoldConfig()
  }
}