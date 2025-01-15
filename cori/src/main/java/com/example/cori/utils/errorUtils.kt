package com.example.cori.utils

import com.example.cori.constants.ErrorMessages
import java.net.UnknownHostException

fun <T> handleException(e : Exception) : com.example.domain.common.Result<T> {

  return when (e) {
    // ! bakal di handle ama cache
    is UnknownHostException -> com.example.domain.common.errorResult(ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE)


    else                    -> com.example.domain.common.errorResult(
      e.localizedMessage ?: ErrorMessages.UNKNOWN_ERROR
    )
  }
}