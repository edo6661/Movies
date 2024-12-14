package com.example.submissionexpert1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.submissionexpert1.presentation.navigation.graph.NavGraph
import com.example.submissionexpert1.presentation.ui.scaffold.MainScaffold
import com.example.submissionexpert1.presentation.ui.scaffold.config.scaffoldConfig
import com.example.submissionexpert1.presentation.ui.theme.SubmissionExpert1Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState : Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SubmissionExpert1Theme {
        val navController = rememberNavController()
        val currentRoute =
          navController.currentBackStackEntryAsState().value?.destination?.route

        fun isActiveRoute(route : String) = currentRoute == route

        MainScaffold(
          isActive = { route -> isActiveRoute(route) },
          navController = navController,
          scaffoldConfig = scaffoldConfig(currentRoute),
          currentRoute = currentRoute
        ) {
          NavGraph(
            modifier = Modifier.padding(it),
            navController = navController
          )
        }
      }
    }
  }
}
