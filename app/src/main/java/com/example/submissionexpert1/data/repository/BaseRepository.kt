package com.example.submissionexpert1.data.repository

import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.data.utils.parseErrorBody
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.common.errorResult
import com.example.submissionexpert1.domain.common.handleException
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
      handleException(e) ?: errorResult(ErrorMessages.NO_INTERNET_CONNECTION)
    }
  }

  protected fun <T, R> Result<T>.mapSuccessResult(onMap : (T) -> R) : Result<R> {
    Result.Loading
    return when (this) {
      is Result.Success -> Result.Success(onMap(this.data))
      is Result.Error   -> Result.Error(this.message)
      is Result.Loading -> Result.Loading
      is Result.Alert   -> Result.Alert(this.message)
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