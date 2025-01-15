package com.example.submissionexpert1.presentation.ui.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.presentation.common.Size

@Composable
fun MainTextField(
  modifier : Modifier = Modifier.fillMaxWidth(),
  value : String,
  onValueChange : (String) -> Unit,
  label : String? = null,
  placeholder : String? = null,
  error : String? = null,

  enabled : Boolean = true,
  readOnly : Boolean = false,
  maxLines : Int = 1,
  keyboardOptions : KeyboardOptions = KeyboardOptions.Default,
  textStyle : TextStyle = TextStyle.Default,

  leadingIcon : @Composable (() -> Unit)? = null,
  trailingIcon : @Composable (() -> Unit)? = null,
  shape : RoundedCornerShape = RoundedCornerShape(8.dp),
  unfocusedTextColor : Color = MaterialTheme.colorScheme.onSecondary,
  focusedTextColor : Color = MaterialTheme.colorScheme.onSecondary,
  unfocusedContainerColor : Color = Color.Unspecified,
  focusedContainerColor : Color = Color.Unspecified,
  unfocusedLabelColor : Color = Color.Unspecified,
  focusedLabelColor : Color = Color.Unspecified,
  cursorColor : Color = Color.Unspecified,
  unfocusedPlaceholderColor : Color = Color.Unspecified,
  focusedPlaceholderColor : Color = Color.Unspecified,
  unfocusedIndicatorColor : Color = Color.Transparent,
  focusedIndicatorColor : Color = Color.Transparent,
  unfocusedLeadingIconColor : Color = Color.Unspecified,
  focusedLeadingIconColor : Color = Color.Unspecified,
  unfocusedTrailingIconColor : Color = Color.Unspecified,
  focusedTrailingIconColor : Color = Color.Unspecified,
  isPasswordVisible : Boolean? = null,

  ) {
  Column {
    TextField(
      value = value,
      onValueChange = onValueChange,
      label = { label?.let { SupportTextField(label) } },
      placeholder = { placeholder?.let { SupportTextField(placeholder) } },
      isError = error != null,
      enabled = enabled,
      readOnly = readOnly,
      maxLines = maxLines,
      modifier = modifier,
      keyboardOptions = keyboardOptions,
      textStyle = textStyle,
      leadingIcon = leadingIcon,
      trailingIcon = trailingIcon,
      singleLine = maxLines == 1,
      shape = if (error != null) RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp
      ) else shape,
      visualTransformation = if (isPasswordVisible == true || isPasswordVisible == null) VisualTransformation.None else PasswordVisualTransformation(),


      colors = TextFieldDefaults.colors(
        unfocusedTextColor = unfocusedTextColor,
        focusedTextColor = focusedTextColor,
        unfocusedContainerColor = unfocusedContainerColor,
        focusedContainerColor = focusedContainerColor,
        unfocusedLabelColor = unfocusedLabelColor,
        focusedLabelColor = focusedLabelColor,
        cursorColor = cursorColor,
        unfocusedPlaceholderColor = unfocusedPlaceholderColor,
        focusedPlaceholderColor = focusedPlaceholderColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedLeadingIconColor = unfocusedLeadingIconColor,
        focusedLeadingIconColor = focusedLeadingIconColor,
        unfocusedTrailingIconColor = unfocusedTrailingIconColor,
        focusedTrailingIconColor = focusedTrailingIconColor,

        )

    )
    ErrorTextField(
      text = error ?: "",
      isVisible = error != null
    )
  }

}

@Composable
private fun SupportTextField(
  text : String
) {
  MainText(
    text = text,
    color = MaterialTheme.colorScheme.onSecondary,
    textSize = Size.Medium

  )
}

@Composable
private fun ErrorTextField(
  text : String,
  isVisible : Boolean

) {
  AnimatedVisibility(
    visible = isVisible
  ) {
    MainText(
      text = text,
      color = MaterialTheme.colorScheme.error,
      textSize = Size.Small
    )
  }
}

@Composable
fun SearchClickableTextField(
  onSearchClick : () -> Unit
) {
  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
      .clickable {
        onSearchClick()
      }
      .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp))
      .fillMaxWidth()
      .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    MainText(
      text = "Search",
      textSize = Size.Medium,
      color = MaterialTheme.colorScheme.onSecondary,
    )
    Icon(
      imageVector = Icons.Default.Search,
      contentDescription = "Search",
      tint = MaterialTheme.colorScheme.onSecondary,
    )

  }
}