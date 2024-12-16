package com.example.submissionexpert1.core.utils

import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.common.errorResult
import java.net.UnknownHostException

fun <T> handleException(e : Exception) : Result<T> {
  // TODO: nanti delete
//  e.printStackTrace()

  return when (e) {
    // ! bakal di handle ama cache
    is UnknownHostException -> errorResult(ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE)


    else                    -> errorResult(e.localizedMessage ?: ErrorMessages.UNKNOWN_ERROR)
  }
}