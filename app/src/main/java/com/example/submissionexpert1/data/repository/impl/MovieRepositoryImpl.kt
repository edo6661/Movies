package com.example.submissionexpert1.data.repository.impl

import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.data.api.ApiService
import com.example.submissionexpert1.data.db.dao.PaginationDao
import com.example.submissionexpert1.data.db.dao.relation.MoviePaginationDao
import com.example.submissionexpert1.data.helper.mapper.toDatabaseEntities
import com.example.submissionexpert1.data.helper.mapper.toDomain
import com.example.submissionexpert1.data.repository.BaseRepository
import com.example.submissionexpert1.data.source.remote.response.PaginationMovieResponse
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.PaginationMovie
import com.example.submissionexpert1.domain.repository.movie.IMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
  private val apiService : ApiService,
  private val paginationDao : PaginationDao,
  private val moviePaginationDao : MoviePaginationDao
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
        insertAllToDao(result.data)
        val cachedData = getCachedMovie(page.toInt())
        emit(cachedData)


      }

      is Result.Error   -> {
        val message = result.message
        if (message == ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE) {
          emitAll(emitCachedMovie(page, result))
        } else {
          emit(Result.Error(result.message))
        }


      }

      is Result.Loading -> {
        emit(Result.Loading)
      }

    }
  }

  private fun emitCachedMovie(
    page : String,
    result : Result.Error
  ) : Flow<Result<PaginationMovie>> = flow {
    val cachedData = getCachedMovie(page.toInt())
    val isDataNull = cachedData is Result.Error
    emit(cachedData)
    if (page.toInt() == 1 && ! isDataNull) {
      emit(Result.Error(result.message))
    }
  }


  private suspend fun getCachedMovie(page : Int) : Result<PaginationMovie> {
    val result = safeDatabaseCall {
      paginationDao.getPaginationWithMovies(page)
        .firstOrNull()
        ?.toDomain()
    }
    return when (result) {
      is Result.Success -> {
        result.data?.let {
          Result.Success(it)
        } ?: Result.Error(ErrorMessages.NO_INTERNET_CONNECTION)
      }

      is Result.Error   -> Result.Error(result.message)
      is Result.Loading -> Result.Loading
    }
  }


  private suspend fun insertAllToDao(data : PaginationMovieResponse) {
    val (paginationEntity, movieEntities, paginationMovieEntities) = data.toDatabaseEntities()
    safeDatabaseCall {
      moviePaginationDao.insertMoviesWithPagination(
        pagination = paginationEntity,
        movies = movieEntities,
        paginationMovies = paginationMovieEntities
      )
    }
  }


}