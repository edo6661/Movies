package com.example.submissionexpert1.core.utils

fun safeUiString(value : String) : String {
  return if (value.length >= 2) {
    value
  } else {
    "N/A"
  }
}