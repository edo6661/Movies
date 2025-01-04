package com.example.submissionexpert1.presentation.navigation.graph

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.submissionexpert1.presentation.implementation.auth.LoginScreen
import com.example.submissionexpert1.presentation.implementation.auth.RegisterScreen
import com.example.submissionexpert1.presentation.navigation.Screen

fun NavGraphBuilder.authNavGraph(
  modifier : Modifier,
  navController : NavHostController
) {
  navigation(
    startDestination = Screen.Auth.Login.route,
    route = "auth"
  ) {
    composable(Screen.Auth.Login.route) {
      val onNavigateRegister = {
        navController.navigate(Screen.Auth.Register.route)
      }
      val onSuccessfulLogin = {
        navController.navigate(Screen.Home.route)
      }
      LoginScreen(
        modifier = modifier,
        onNavigateRegister = onNavigateRegister,
        onSuccessfulLogin = onSuccessfulLogin

      )
    }
    composable(Screen.Auth.Register.route) {
      val onNavigateLogin = {
        navController.navigate(Screen.Auth.Login.route)
      }
      RegisterScreen(
        modifier = modifier,
        onNavigateLogin = onNavigateLogin
      )
    }
  }
}