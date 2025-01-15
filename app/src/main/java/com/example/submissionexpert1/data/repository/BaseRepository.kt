package com.example.submissionexpert1.data.repository

import com.example.cori.constants.ErrorMessages
import com.example.cori.utils.handleException
import com.example.domain.common.Result
import com.example.domain.common.errorResult
import com.example.domain.common.successResult
import com.example.submissionexpert1.data.utils.parseErrorBody
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
          }
          ?: errorResult(ErrorMessages.EMPTY_RESPONSE)
        }

        else             -> {
          val errBody = parseErrorBody(
            res.errorBody()?.string()
          )

          errorResult(
            errBody?.statusMessage ?: ErrorMessages.UNKNOWN_ERROR
          )

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