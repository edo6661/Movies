package com.example.submissionexpert1.presentation.navigation

sealed class Screen(val route : String) {
  object Home : Screen("Home")
  data class Detail(val id : String) : Screen("Detail/$id")
  object Favorite : Screen("Favorite")
}