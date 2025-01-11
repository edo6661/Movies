package com.example.submissionexpert1.presentation.ui.shared

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.presentation.common.Size

@Composable
fun MainButton(
  modifier : Modifier = Modifier,
  text : String,
  size : Size = Size.Medium,
  color : Color = MaterialTheme.colorScheme.onBackground,
  backgroundColor : Color = MaterialTheme.colorScheme.tertiary,
  fontWeight : FontWeight = FontWeight.Bold,
  isEnabled : Boolean = true,
  onClick : () -> Unit
) {
  val buttonSize = when (size) {
    Size.Small      -> ButtonDefaults.MinWidth
    Size.Medium     -> ButtonDefaults.MinWidth + 24.dp
    Size.Large      -> ButtonDefaults.MinWidth + 48.dp
    Size.ExtraLarge -> ButtonDefaults.MinWidth + 72.dp
  }

  Button(
    onClick = onClick,
    modifier = modifier,
    colors = ButtonDefaults.buttonColors(
      containerColor = backgroundColor,
      contentColor = color
    ),
    enabled = isEnabled,
    contentPadding = PaddingValues(horizontal = buttonSize / 4)
  ) {
    MainText(
      text = text,
      textSize = size,
      color = color,
      fontWeight = fontWeight,
      isEllipsis = false,
      maxLines = 1,
      textAlign = TextAlign.Center
    )
  }
}

