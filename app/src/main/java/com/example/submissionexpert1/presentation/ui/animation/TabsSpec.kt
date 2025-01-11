package com.example.submissionexpert1.presentation.ui.animation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

fun TabsSpec(
  targetState : Int,
  initialState : Int
) : ContentTransform {
  return if (targetState > initialState) {
    slideInHorizontally(
      initialOffsetX = { 1000 },
      animationSpec = tween(800)
    ) togetherWith slideOutHorizontally(
      targetOffsetX = { - 1000 },
      animationSpec = tween(800)
    )
  } else {
    slideInHorizontally(
      initialOffsetX = { - 1000 },
      animationSpec = tween(800)
    ) togetherWith slideOutHorizontally(
      targetOffsetX = { 1000 },
      animationSpec = tween(800)
    )
  }
}