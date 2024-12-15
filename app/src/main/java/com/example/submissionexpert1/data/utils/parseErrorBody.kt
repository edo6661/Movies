package com.example.submissionexpert1.data.utils

import android.util.Log
import com.example.submissionexpert1.data.api.ApiError
import com.google.gson.Gson

fun parseErrorBody(errorBody : String?) : ApiError? {
  return try {
    errorBody?.let {
      Gson().fromJson(it, ApiError::class.java)
    }
  } catch (e : Exception) {
    // TODO: nanti delete
    Log.e("BaseRepository", "Error parsing errorBody: ${e.localizedMessage}")
    null
  }
}
