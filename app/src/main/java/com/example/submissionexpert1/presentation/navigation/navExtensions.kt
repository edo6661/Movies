package com.example.submissionexpert1.presentation.navigation

import androidx.navigation.NavController

fun NavController.navigateSingleTop(route : String) {
  navigate(route) {
    popUpTo(route) { inclusive = true }
    launchSingleTop = true
    restoreState = true
  }
}

fun NavController.navigateClearStack(route : String) {
  navigate(route) {
    popUpTo(graph.startDestinationId) { inclusive = true }
    launchSingleTop = true
  }
}