package com.example.submissionexpert1.presentation.implementation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.submissionexpert1.presentation.ui.shared.MainSearchBar
import com.example.submissionexpert1.presentation.ui.shared.MainText

@Composable
fun SearchScreen(
  modifier : Modifier,
  onNavigateDetail : (String) -> Unit,
  onNavigateBack : () -> Unit,
) {

  var query by remember { mutableStateOf("") }
  var active by remember { mutableStateOf(true) }


  LaunchedEffect(active) {
    if (! active) {
      onNavigateBack()
    }
  }



  Column(
    modifier = modifier
  ) {
    MainSearchBar(
      query = query,
      onQueryChange = {
        query = it
      },
      onSearch = {
        Log.d("SearchScreen", "Search for $query")
      },
      active = active,
      onActiveChange = {
        active = it
      },
      enabled = true,
      allowKeyboard = true,
      modifier = Modifier.fillMaxWidth(),
      callbackTrailingIcon = {
        if (query.isNotEmpty()) {
          query = ""
        } else {
          onNavigateBack()
        }
      }

    ) {
      Column(
        modifier = Modifier.padding(
          horizontal = 16.dp
        )
      ) {
        MainText(
          text = "Search Screen",
          modifier = Modifier.fillMaxWidth()
        )
      }
    }


  }
}