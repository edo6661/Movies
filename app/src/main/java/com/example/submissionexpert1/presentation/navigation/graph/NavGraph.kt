package com.example.submissionexpert1.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.submissionexpert1.presentation.implementation.*
import com.example.submissionexpert1.presentation.navigation.ProfileScreen
import com.example.submissionexpert1.presentation.navigation.Screen
import com.example.submissionexpert1.presentation.navigation.navigateClearStack
import com.example.submissionexpert1.presentation.navigation.navigateSingleTop

@Composable
fun NavGraph(
  modifier : Modifier = Modifier,
  startDestination : String = Screen.Home.route,
  navController : NavHostController,
  isUserLoggedIn : Boolean = false,
) {
  NavHost(
    navController = navController,
    startDestination = startDestination,
  ) {
    authNavGraph(
      modifier = modifier,
      navController = navController
    )
//    composable(
//      route = Screen.Splash.route
//    ) {
//      SplashScreen(
//        modifier = modifier,
//        onSplashFinished = {
//          navController.navigateClearStack(Screen.Home.route)
//        }
//      )
//    }
    val onNavigateDetail = { id : String ->
      navController.navigateSingleTop("Detail/$id")
    }
    composable(
      route = Screen.Home.route
    ) {
      val navigateToLogin = {
        navController.navigateSingleTop(Screen.Auth.Login.route)
      }
      val navigateToSearch = {
        navController.navigateSingleTop(Screen.Search.route)
      }


      HomeScreen(
        modifier = modifier,
        onNavigateDetail = onNavigateDetail,
        navigateToLogin = navigateToLogin,
        navigateToSearch = navigateToSearch
      )
    }
    composable(route = Screen.Favorite.route) {
      FavoriteScreen(
        modifier = modifier,
        onNavigateDetail = onNavigateDetail
      )
    }

    composable(
      route = Screen.Settings.route
    ) {
      SettingsScreen(
        modifier = modifier,
        navigateToProfile = {
          navController.navigateSingleTop(Screen.Profile.route)
        },
        navigateToTheme = {
          navController.navigateSingleTop(Screen.Theme.route)
        },
        onNavigateLogout = {
          navController.navigateClearStack(Screen.Home.route)
        }
      )
    }
    composable(
      route = Screen.Profile.route
    ) {
      ProfileScreen(
        modifier = modifier,
        onNavigateLogin = {
          navController.navigateSingleTop(Screen.Auth.Login.route)
        }
      )
    }
    composable(
      route = Screen.Theme.route
    ) {
      ThemeScreen(
        modifier = modifier,
      )
    }
    composable(
      route = Screen.Search.route,
    ) {
      SearchScreen(
        modifier = modifier,
        onNavigateDetail = onNavigateDetail,

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
        id = it.arguments?.getString("id")?.toIntOrNull() ?: 0,
        navigateToLogin = {
          navController.navigateSingleTop(Screen.Auth.Login.route)
        },
        onNavigateBack = {
          navController.popBackStack()
        }
      )
    }
  }
}