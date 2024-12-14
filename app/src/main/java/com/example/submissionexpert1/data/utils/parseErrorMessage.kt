package com.example.submissionexpert1.data.utils

import com.example.submissionexpert1.core.constants.ErrorMessages
import org.json.JSONObject

fun parseErrorMessage(errorBody : String?) : String {
  return try {
    val jsonObject = JSONObject(errorBody ?: "")
    jsonObject.optString("message", ErrorMessages.UNKNOWN_ERROR)
  } catch (e : Exception) {
    ErrorMessages.UNKNOWN_ERROR
  }
}