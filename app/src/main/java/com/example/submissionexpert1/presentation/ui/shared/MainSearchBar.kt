package com.example.submissionexpert1.presentation.ui.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSearchBar(
  query : String,
  onQueryChange : (String) -> Unit,
  onSearch : (String) -> Unit,
  active : Boolean,
  onActiveChange : (Boolean) -> Unit,
  modifier : Modifier = Modifier,
  enabled : Boolean = true,
  placeholderText : String = "Cari...",
  allowKeyboard : Boolean = false,
  callbackTrailingIcon : () -> Unit = {},
  content : @Composable () -> Unit = {}
) {
  val interactionSource = remember { MutableInteractionSource() }
  val focusManager = LocalFocusManager.current
  val focusRequester = remember { FocusRequester() }

  SearchBar(
    query = query,
    onQueryChange = onQueryChange,
    onSearch = onSearch,
    active = active,
    onActiveChange = onActiveChange,
    modifier = if (! allowKeyboard) {
      modifier
        .clickable(
          interactionSource = interactionSource,
          indication = null
        ) { onActiveChange(true) }
        .onFocusChanged { focusState ->
          if (focusState.isFocused) {
            focusManager.clearFocus()
          }
        }
    } else {
      modifier.focusRequester(focusRequester)
    },
    enabled = enabled,
    interactionSource = if (! allowKeyboard) interactionSource else remember { MutableInteractionSource() },
    placeholder = { Text(placeholderText) },
    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
    trailingIcon = {
      if (active) {
        IconButton(onClick = {
          callbackTrailingIcon()
        }) {
          Icon(Icons.Default.Close, "Clear search")
        }
      }
    },
    colors = SearchBarDefaults.colors(
      containerColor = MaterialTheme.colorScheme.surface,
      dividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),

      )
  ) {
    Spacer(modifier = Modifier.height(24.dp))
    content(

    )


  }

  LaunchedEffect(allowKeyboard, active) {
    if (allowKeyboard && active) {
      delay(100)
      focusRequester.requestFocus()
    }
  }
}
