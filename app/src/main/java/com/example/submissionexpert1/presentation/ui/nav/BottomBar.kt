package com.example.submissionexpert1.presentation.ui.nav

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.submissionexpert1.domain.model.User
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.navigation.navigateSingleTop
import com.example.submissionexpert1.presentation.ui.shared.MainText

@Composable
fun BottomBar(
  isActive : (String) -> Boolean,
  navController : NavHostController,
  user : User? = null,
) {
  Column(

  ) {

    HorizontalDivider(
      modifier = Modifier
        .offset(y = (- 16).dp)
        .background(MaterialTheme.colorScheme.secondary)
        .fillMaxWidth()
        .height(2.5.dp)
    )
    NavigationBar(
      containerColor = MaterialTheme.colorScheme.background,

      ) {
      Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
          .fillMaxWidth()
          .height(80.dp),
      ) {
        BottomBarItem().bottomBarItems(isUserLoggedIn = user != null)
          .forEachIndexed { _, navigationItem ->
            val isActiveRoute = isActive(navigationItem.route)
            val color by animateColorAsState(
              targetValue = if (isActiveRoute) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
              label = "Color Bottom Bar"
            )
            Column(
              verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .weight(1f)
                .clickable {
                  if (isActiveRoute) return@clickable
                  navController.navigateSingleTop(navigationItem.route)
                }
                .padding(
                  vertical = 8.dp,
                )
            ) {
              Icon(
                imageVector = navigationItem.icon,
                contentDescription = navigationItem.label,
                tint = color,
                modifier = Modifier.size(32.dp),
              )
              MainText(
                text = navigationItem.label,
                color = color,
                textSize = Size.Medium
              )
            }
          }

        /*
        NavigationBarItem(
          selected = isActive(navigationItem.route),
          label = {
            MainText(
              text = navigationItem.label,
              color = MaterialTheme.colorScheme.secondary,
              textSize = Size.Medium,
            )
          },
          icon = {
            Icon(
              navigationItem.icon,
              contentDescription = navigationItem.label,
              tint = if (isActive(navigationItem.route)) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
            )
          },
          onClick = {
            navController.navigateSingleTop(navigationItem.route)
          },
          alwaysShowLabel = false,
          enabled = ! isActive(navigationItem.route),
        )
         */
      }

    }
  }
}