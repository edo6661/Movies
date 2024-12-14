package com.example.submissionexpert1.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.submissionexpert1.presentation.implementation.FavoriteScreen
import com.example.submissionexpert1.presentation.implementation.HomeScreen
import com.example.submissionexpert1.presentation.navigation.Screen

@Composable
fun NavGraph(
  modifier : Modifier = Modifier,
  startDestination : String = Screen.Home.route,
  navController : NavHostController
) {
  NavHost(
    navController = navController,
    startDestination = startDestination,
  ) {
    val onNavigateDetail = { id : String ->
      navController.navigate("Detail/$id")
    }
    composable(
      route = Screen.Home.route
    ) {

      HomeScreen(
        modifier = modifier,
        onNavigateDetail = onNavigateDetail
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
      FavoriteScreen(
        modifier = modifier,
        onNavigateDetail = onNavigateDetail
      )
    }
  }
}