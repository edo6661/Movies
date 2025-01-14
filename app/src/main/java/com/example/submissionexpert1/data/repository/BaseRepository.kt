package com.example.submissionexpert1.data.repository

import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.core.utils.handleException
import com.example.submissionexpert1.data.utils.parseErrorBody
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
          val errBody = parseErrorBody(
            res.errorBody()?.string()
          )

          errorResult(errBody?.statusMessage ?: ErrorMessages.UNKNOWN_ERROR)

        }
      }
    } catch (e : Exception) {
      handleException(e)
    }
  }

  protected suspend fun <T> safeDatabaseCall(
    databaseCall : suspend () -> T
  ) : Result<T> {
    return try {
      val result = databaseCall.invoke()
      successResult(result)
    } catch (e : Exception) {
      errorResult(
        e.localizedMessage ?: ErrorMessages.UNKNOWN_ERROR
      )
    }
  }


}