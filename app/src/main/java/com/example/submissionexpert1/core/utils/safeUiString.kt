package com.example.submissionexpert1.core.utils

fun safeUiString(value : String?) : String {
  return if (value == null || value.length >= 2) {
    value ?: "N/A"
  } else {
    "N/A"
  }
}