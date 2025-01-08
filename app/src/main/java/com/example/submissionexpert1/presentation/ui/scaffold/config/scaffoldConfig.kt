package com.example.submissionexpert1.presentation.ui.scaffold.config

import com.example.submissionexpert1.presentation.navigation.Screen

data class ScaffoldConfig(
  val showMainNav : Boolean = true,
  val showBackNav : Boolean = false,
)

fun scaffoldConfig(currentRoute : String?)
  : ScaffoldConfig {
  return when (currentRoute) {
    "Detail/{id}"       -> ScaffoldConfig(
      showMainNav = false,
      showBackNav = true
    )

    Screen.Search.route -> ScaffoldConfig(
      showMainNav = false,
      showBackNav = false
    )

    else                -> ScaffoldConfig()
  }
}