package com.example.submissionexpert1.presentation.ui.state.alert

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.presentation.ui.shared.MainButton
import com.example.submissionexpert1.presentation.ui.shared.MainText

@Composable
fun BottomAlert(
  onDismiss : () -> Unit,
  message : String,
  visible : Boolean,
  modifier : Modifier = Modifier
) {
  AnimatedVisibility(
    visible = visible,
    modifier = modifier.padding(bottom = 16.dp),
    enter = slideInVertically { it + 200 } + fadeIn(
      initialAlpha = 0.5f,
      animationSpec = tween(durationMillis = 300)
    ),
    exit = slideOutVertically { it + 200 } + fadeOut(
      animationSpec = tween(durationMillis = 300),
      targetAlpha = 0.5f
    )
  ) {

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.shapes.medium)
        .padding(16.dp)
    ) {
      MainText(
        text = message,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
      )
      MainButton(
        text = "Dismiss",
        onClick = {
          onDismiss()
        },
        modifier = Modifier.align(Alignment.End),
      )
    }
  }
}