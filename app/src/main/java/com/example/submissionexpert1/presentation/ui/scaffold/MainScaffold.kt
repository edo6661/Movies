package com.example.submissionexpert1.presentation.ui.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.submissionexpert1.presentation.ui.nav.BottomBar
import com.example.submissionexpert1.presentation.ui.nav.TopBackNav
import com.example.submissionexpert1.presentation.ui.nav.TopNav
import com.example.submissionexpert1.presentation.ui.scaffold.config.ScaffoldConfig

@Composable
fun MainScaffold(
  navController : NavHostController,
  scaffoldConfig : ScaffoldConfig,
  isActive : (String) -> Boolean,
  currentRoute : String?,
  content : @Composable (PaddingValues) -> Unit
) {
  Scaffold(
    topBar = {
      when {
        scaffoldConfig.showBackNav -> {
          TopBackNav(
            title = "Temporary",
            onNavigateBack = { navController.popBackStack() }
          )
        }

        else                       -> {
          TopNav(
            currentRoute = currentRoute,
          )
        }
      }
    },
    bottomBar = {
      if (! scaffoldConfig.showBackNav) {
        BottomBar(
          isActive = isActive,
          navController = navController
        )
      }
    }
  ) { innerPadding ->
    content(innerPadding)
  }

}