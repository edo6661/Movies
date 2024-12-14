package com.example.submissionexpert1.presentation.common

sealed class Size {
  data object Small : Size()
  data object Medium : Size()
  data object Large : Size()
  data object ExtraLarge : Size()
}