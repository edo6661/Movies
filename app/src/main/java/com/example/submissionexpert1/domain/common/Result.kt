package com.example.submissionexpert1.domain.common

import com.example.submissionexpert1.core.constants.AlertMessages
import com.example.submissionexpert1.core.constants.ErrorMessages
import java.net.UnknownHostException

sealed class Result<out T> {
  data object Loading : Result<Nothing>()
  data class Success<T>(val data : T) : Result<T>()
  data class Error(val message : String?) : Result<Nothing>()
  data class Alert(val message : String?) : Result<Nothing>()

}

fun <T> successResult(data : T) : Result<T> {
  return Result.Success(data)
}

fun <T> errorResult(message : String?) : Result<T> {
  return Result.Error(message)
}

fun <T> alertResult(message : String?) : Result<T> {
  return Result.Alert(message)
}

fun <T> handleException(e : Exception) : Result<T> {
  e.printStackTrace()

  return when (e) {
    is UnknownHostException -> alertResult(AlertMessages.NO_INTERNET_CONNECTION_ONLY_CACHE)


    else                    -> errorResult(e.localizedMessage ?: ErrorMessages.UNKNOWN_ERROR)
  }
}