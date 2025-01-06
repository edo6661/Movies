package com.example.submissionexpert1.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.submissionexpert1.presentation.implementation.DetailScreen
import com.example.submissionexpert1.presentation.implementation.FavoriteScreen
import com.example.submissionexpert1.presentation.implementation.HomeScreen
import com.example.submissionexpert1.presentation.navigation.Screen
import com.example.submissionexpert1.presentation.navigation.navigateSingleTop

@Composable
fun NavGraph(
  modifier : Modifier = Modifier,
  startDestination : String = "favorite",
  navController : NavHostController,
  isUserLoggedIn : Boolean = false,
) {
  NavHost(
    navController = navController,
    startDestination = if (isUserLoggedIn) Screen.Favorite.route else startDestination,
  ) {
    authNavGraph(
      modifier = modifier,
      navController = navController
    )
    val onNavigateDetail = { id : String ->
      navController.navigateSingleTop("Detail/$id")
    }
    composable(
      route = Screen.Home.route
    ) {
      val navigateToLogin = {
        navController.navigateSingleTop(Screen.Auth.Login.route)
      }

      HomeScreen(
        modifier = modifier,
        onNavigateDetail = onNavigateDetail,
        navigateToLogin = navigateToLogin
      )
    }
    composable(
      route = Screen.Favorite.route
    ) {
      FavoriteScreen(
        modifier = modifier,
        onNavigateDetail = onNavigateDetail
      )
    }
    composable(
      route = "Detail/{id}",
      arguments = listOf(
        navArgument("id") {
          type = NavType.StringType
        }
      )
    ) {
      DetailScreen(
        modifier = modifier,
      )
    }
  }
}