package com.example.submissionexpert1.presentation.ui.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.domain.model.User
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
  user : User? = null,
  content : @Composable (PaddingValues) -> Unit,
) {
  Scaffold(
    topBar = {
      when {
        scaffoldConfig.showBackNav -> {
          TopBackNav(
            title = if (currentRoute == "Detail/{id}") "Detail" else currentRoute ?: "",
            onNavigateBack = { navController.popBackStack() },
          )
        }

        scaffoldConfig.showMainNav -> {
          TopNav(
            currentRoute = currentRoute,
          )
        }

        else                       -> {
        }
      }
    },
    bottomBar = {
      when {
        scaffoldConfig.showBottomBar -> {
          BottomBar(
            isActive = isActive,
            navController = navController,
            user = user,

            )
        }
      }

    }
  ) { innerPadding ->
    content(innerPadding)
  }

}