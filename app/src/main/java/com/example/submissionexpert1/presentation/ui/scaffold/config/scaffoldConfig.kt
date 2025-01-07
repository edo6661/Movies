package com.example.submissionexpert1.presentation.ui.scaffold.config

data class ScaffoldConfig(
  val showMainNav : Boolean = true,
  val showBackNav : Boolean = false,
)

fun scaffoldConfig(currentRoute : String?)
  : ScaffoldConfig {
  return when (currentRoute) {
    "Detail/{id}" -> ScaffoldConfig(
      showMainNav = false,
      showBackNav = true
    )

    else          -> ScaffoldConfig()
  }
}