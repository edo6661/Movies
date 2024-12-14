package com.example.submissionexpert1.data.repository

import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.data.utils.parseErrorMessage
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.common.errorResult
import com.example.submissionexpert1.domain.common.successResult
import retrofit2.Response

abstract class BaseRepository {

  protected suspend fun <T> safeApiCall(
    apiCall : suspend () -> Response<T>
  ) : Result<T> {
    return try {
      val res = apiCall.invoke()
      when {
        res.isSuccessful -> {
          res.body()?.let {
            successResult(it)
          } ?: errorResult(ErrorMessages.EMPTY_RESPONSE)
        }

        else             -> {
          val errBody = res.errorBody()?.string()
          val errMsg = parseErrorMessage(errBody)
          errorResult(errMsg)
        }
      }
    } catch (e : Exception) {
      e.printStackTrace()
      errorResult(
        e.localizedMessage ?: ErrorMessages.UNKNOWN_ERROR
      )
    }
  }

  protected suspend fun <T> safeDatabaseCall(
    databaseCall : suspend () -> T
  ) : Result<T> {
    return try {
      val result = databaseCall.invoke()
      successResult(result)
    } catch (e : Exception) {
      e.printStackTrace()
      errorResult(
        e.localizedMessage ?: ErrorMessages.UNKNOWN_ERROR
      )
    }
  }


}