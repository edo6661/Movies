package com.example.submissionexpert1.presentation.ui.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.example.submissionexpert1.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun TopNav(
  currentRoute : String?,
) {
  TopAppBar(
    title = {
      Text(text = currentRoute ?: R.string.app_name.toString())
    }
  )

}