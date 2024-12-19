package com.example.submissionexpert1.presentation.navigation

sealed class Screen(val route : String) {
  sealed class Auth(route : String) : Screen(route) {
    object Login : Auth("Login")
    object Register : Auth("Register")
  }

  object Home : Screen("Home")
  data class Detail(val id : String) : Screen("Detail/$id")
  object Favorite : Screen("Favorite")
}