package com.example.submissionexpert1.presentation.navigation

sealed class Screen(val route : String) {
  sealed class Auth(route : String) : Screen(route) {
    data object Login : Auth("Login")
    data object Register : Auth("Register")
  }

  data object Home : Screen("Home")
  data object Favorite : Screen("Favorite")
  data class Detail(val id : String) : Screen("Detail/$id")
  data object Search : Screen("Search")
  data object Settings : Screen("Settings")
}