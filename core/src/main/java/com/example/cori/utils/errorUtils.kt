package com.example.cori.utils

import com.example.cori.constants.ErrorMessages
import com.example.domain.common.Result
import com.example.domain.common.errorResult
import java.net.UnknownHostException

fun <T> handleException(e : Exception) : Result<T> {

  return when (e) {
    // ! bakal di handle ama cache
    is UnknownHostException -> errorResult(ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE)


    else                    -> errorResult(
      e.localizedMessage ?: ErrorMessages.UNKNOWN_ERROR
    )
  }
}