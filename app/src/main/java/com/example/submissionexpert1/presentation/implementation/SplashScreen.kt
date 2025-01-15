package com.example.submissionexpert1.presentation.implementation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.submissionexpert1.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  modifier : Modifier = Modifier,
  onSplashFinished : () -> Unit
) {
  var startAnimation by remember { mutableStateOf(false) }
  val alphaAnim = animateFloatAsState(
    targetValue = if (startAnimation) 1f else 0f,
    animationSpec = tween(
      durationMillis = 1000
    ),
    label = "Alpha Animation"
  )

  LaunchedEffect(key1 = true) {
    startAnimation = true
    delay(2000L)
    onSplashFinished()
  }

  Image(
    modifier = modifier
      .fillMaxSize(),
    painter = painterResource(id = R.drawable.splash),
    contentDescription = "Logo",
    alpha = alphaAnim.value,
    alignment = Alignment.Center,
    contentScale = ContentScale.FillWidth

  )
}
