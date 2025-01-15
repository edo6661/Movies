package com.example.submissionexpert1.data.repository

import com.example.cori.constants.ErrorMessages
import com.example.cori.utils.handleException
import com.example.submissionexpert1.data.utils.parseErrorBody
import retrofit2.Response

abstract class BaseRepository {

  protected suspend fun <T> safeApiCall(
    apiCall : suspend () -> Response<T>
  ) : com.example.domain.common.Result<T> {
    return try {
      val res = apiCall.invoke()
      when {
        res.isSuccessful -> {
          res.body()?.let {
            com.example.domain.common.successResult(it)
          }
          ?: com.example.domain.common.errorResult(com.example.cori.constants.ErrorMessages.EMPTY_RESPONSE)
        }

        else             -> {
          val errBody = parseErrorBody(
            res.errorBody()?.string()
          )

          com.example.domain.common.errorResult(
            errBody?.statusMessage ?: com.example.cori.constants.ErrorMessages.UNKNOWN_ERROR
          )

        }
      }
    } catch (e : Exception) {
      com.example.cori.utils.handleException(e)
    }
  }

  protected suspend fun <T> safeDatabaseCall(
    databaseCall : suspend () -> T
  ) : com.example.domain.common.Result<T> {
    return try {
      val result = databaseCall.invoke()
      com.example.domain.common.successResult(result)
    } catch (e : Exception) {
      com.example.domain.common.errorResult(
        e.localizedMessage ?: com.example.cori.constants.ErrorMessages.UNKNOWN_ERROR
      )
    }
  }


}