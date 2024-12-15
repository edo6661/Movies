package com.example.submissionexpert1.data.repository.impl

import com.example.submissionexpert1.core.constants.AlertMessages
import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.data.api.ApiService
import com.example.submissionexpert1.data.db.dao.MovieDao
import com.example.submissionexpert1.data.db.dao.PaginationDao
import com.example.submissionexpert1.data.db.entity.relation.PaginationMovieEntity
import com.example.submissionexpert1.data.helper.mapper.toDomain
import com.example.submissionexpert1.data.helper.mapper.toMovieEntity
import com.example.submissionexpert1.data.helper.mapper.toPaginationEntity
import com.example.submissionexpert1.data.repository.BaseRepository
import com.example.submissionexpert1.data.source.remote.response.PaginationMovieResponse
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.PaginationMovie
import com.example.submissionexpert1.domain.repository.movie.IMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
  private val apiService : ApiService,
  private val movieDao : MovieDao,
  private val paginationDao : PaginationDao
) : IMovieRepository, BaseRepository() {

  override fun getPopularMovies(
    page : String,
  ) : Flow<Result<PaginationMovie>> = flow {
    val result = safeApiCall {
      apiService.getPopularMovies(
        page = page,
        // TODO: replace dengan dynamic language (pake shared preference / data store)
        language = "en-US",
      )
    }
    when (result) {
      is Result.Success -> {
        val data = result.data
        insertAllToDao(data)
        emit(Result.Success(data.toDomain()))
      }

      is Result.Error   -> {
        if (result.message == ErrorMessages.NO_INTERNET_CONNECTION) {
          val data = getPaginationMovieEntity(page.toInt())
          emit(data)
          emit(
            Result.Alert(AlertMessages.NO_INTERNET_CONNECTION_ONLY_CACHE)

          )

        } else {
          emit(Result.Error(result.message))
        }

      }

      is Result.Loading -> {
        emit(Result.Loading)
      }

      is Result.Alert   -> {
        emit(Result.Alert(result.message))
      }

    }
  }

  private suspend fun insertAllToDao(
    data : PaginationMovieResponse
  ) {
    val paginationEntity = data.toPaginationEntity()
    val movieEntities = data.results.map { it.toMovieEntity() }
    val paginationMovieEntities = data.results.map {
      PaginationMovieEntity(page = data.page, movieId = it.id)
    }
    safeDatabaseCall {
      paginationDao.insertPagination(paginationEntity)
      movieDao.insertMovies(movieEntities)
      paginationDao.insertPaginationMovies(paginationMovieEntities)
    }
  }

  private suspend fun getPaginationMovieEntity(page : Int) : Result<PaginationMovie> {
    val result = safeDatabaseCall {
      paginationDao.getPaginationWithMovies(page)?.toDomain()
    }

    return when (result) {
      is Result.Success -> {
        result.data?.let {
          Result.Success(it)

        }
        ?: Result.Error("${ErrorMessages.EMPTY_DATA_FROM_CACHE} and ${ErrorMessages.NO_INTERNET_CONNECTION}")
      }

      is Result.Error   -> {
        Result.Error(result.message)
      }

      is Result.Loading -> {
        Result.Loading
      }

      is Result.Alert   -> {
        Result.Alert(result.message)
      }
    }
  }


}