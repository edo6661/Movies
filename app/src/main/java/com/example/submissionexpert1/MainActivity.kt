package com.example.submissionexpert1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.submissionexpert1.data.source.local.preferences.ThemePreferences
import com.example.submissionexpert1.presentation.navigation.graph.NavGraph
import com.example.submissionexpert1.presentation.ui.scaffold.MainScaffold
import com.example.submissionexpert1.presentation.ui.scaffold.config.scaffoldConfig
import com.example.submissionexpert1.presentation.ui.theme.SubmissionExpert1Theme
import com.example.submissionexpert1.presentation.viewmodel.auth.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val vm : MainViewModel by viewModels()


  @Inject
  lateinit var themePreferences : ThemePreferences

  override fun onCreate(savedInstanceState : Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      SubmissionExpert1Theme(
        themePreferences = themePreferences,
      ) {

        val state by vm.state.collectAsState()

        val navController = rememberNavController()
        val currentRoute =
          navController.currentBackStackEntryAsState().value?.destination?.route

        fun isActiveRoute(route : String) = currentRoute == route

        MainScaffold(
          isActive = { route -> isActiveRoute(route) },
          navController = navController,
          scaffoldConfig = scaffoldConfig(currentRoute),
          currentRoute = currentRoute,

          user = state.user,

          ) {
          NavGraph(
            modifier = Modifier
              .padding(it),
            navController = navController,
            isUserLoggedIn = state.user != null,
          )
        }
      }
    }
  }
}
