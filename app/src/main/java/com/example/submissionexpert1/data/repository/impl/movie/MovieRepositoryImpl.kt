package com.example.submissionexpert1.data.repository.impl.movie

import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.data.api.ApiService
import com.example.submissionexpert1.data.db.dao.MovieDao
import com.example.submissionexpert1.data.db.dao.PaginationDao
import com.example.submissionexpert1.data.db.dao.relation.FavoriteMoviePaginationDao
import com.example.submissionexpert1.data.db.dao.relation.MoviePaginationDao
import com.example.submissionexpert1.data.helper.mapper.toDatabaseEntities
import com.example.submissionexpert1.data.helper.mapper.toDomain
import com.example.submissionexpert1.data.helper.mapper.toDomainWithFavorite
import com.example.submissionexpert1.data.repository.BaseRepository
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import com.example.submissionexpert1.data.source.remote.response.PaginationMovieResponse
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.Movie
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
  private val moviePaginationDao : MoviePaginationDao,
  private val favoriteMovieDao : FavoriteMoviePaginationDao,
  private val movieDao : MovieDao,
  private val userPreferences : UserPreferences
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
    emitAll(handleApiResult(result, page))

  }


  private suspend fun handleApiResult(
    result : Result<PaginationMovieResponse>,
    page : String
  ) : Flow<Result<PaginationMovie>> = flow {
    when (result) {
      is Result.Success -> {
        val isPaginationExist = paginationDao.getPagination(page.toInt()).firstOrNull() != null
        if (! isPaginationExist) {
          insertAllToDao(result.data)
        }
        val cachedData = getCachedMovie(page.toInt())
        emit(cachedData)
      }

      is Result.Error   -> {
        val message = result.message
        // ! nge handle unknown host exception
        if (message == ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE) {
          emitAll(getCachedMovieWhenError(page, result))
        } else {
          emit(Result.Error(result.message))
        }
      }

      is Result.Loading -> {
        emit(Result.Loading)
      }

    }
  }

  private fun getCachedMovieWhenError(
    page : String,
    result : Result.Error
  ) : Flow<Result<PaginationMovie>> = flow {
    val cachedData = getCachedMovie(page.toInt())
    val isDataNotNullAndFirstPage = cachedData !is Result.Error && page.toInt() == 1
    emit(cachedData)
    if (isDataNotNullAndFirstPage) {
      emit(Result.Error(result.message))
    }
  }


  private suspend fun getCachedMovie(page : Int) : Result<PaginationMovie> {
    val userId = when (val resultUserId = getUserId()) {
      is Result.Success -> resultUserId.data
      is Result.Error   -> return Result.Error(resultUserId.message)
      is Result.Loading -> return Result.Loading
    }
    val result = safeDatabaseCall {
      paginationDao.getPaginationWithMovies(page, userId)
        .firstOrNull()
        ?.toDomainWithFavorite()
    }

    return when (result) {
      is Result.Success -> {
        result.data?.let {
          Result.Success(it)
          // ! ini case nya padahal data cache nya itu null tapi kok error nya no internet connection? karena dia nge handle UnknownHostException, jadi cuman nerusin aja
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

  override fun getPopularMoviesFavorite(
    page : String,
  ) : Flow<Result<PaginationMovie>> = flow {
    emit(Result.Loading)
    val userId = when (val resultUserId = getUserId()) {
      is Result.Success -> resultUserId.data

      is Result.Error   -> {
        emit(Result.Error(resultUserId.message))
        return@flow
      }

      is Result.Loading -> {
        emit(Result.Loading)
        return@flow
      }
    }
    val result = safeDatabaseCall {
      favoriteMovieDao.getFavoriteMoviesByUser(page.toInt(), userId)
        .firstOrNull()
        ?.toDomainWithFavorite()
    }

    when (result) {
      is Result.Success -> {
        result.data?.let {
          emit(Result.Success(it))
        } ?: emit(Result.Error(ErrorMessages.CANT_FETCH_MORE))
      }

      is Result.Error   -> {
        emit(Result.Error(result.message))
      }

      is Result.Loading -> {
        emit(Result.Loading)
      }
    }
  }

  override fun getMovie(id : Int) : Flow<Result<Movie>> = flow {
    emit(Result.Loading)
    val userId = when (val resultUserId = getUserId()) {
      is Result.Success -> resultUserId.data

      is Result.Error   -> {
        emit(Result.Error(resultUserId.message))
        return@flow
      }

      is Result.Loading -> {
        emit(Result.Loading)
        return@flow
      }
    }
    
    val isMovieFavorite = when (safeDatabaseCall {
      favoriteMovieDao.isMovieFavorite(
        userId = userId,
        movieId = id
      )
    }) {
      is Result.Success -> true
      is Result.Error   -> false
      is Result.Loading -> false
    }
    val result = safeDatabaseCall {
      movieDao.getMovieById(id)
        .firstOrNull()
        ?.toDomain()
        ?.copy(
          isFavorite = isMovieFavorite
        )

    }

    when (result) {
      is Result.Success -> {
        result.data?.let {
          emit(Result.Success(it))
        } ?: emit(Result.Error(ErrorMessages.DATA_NOT_FOUND))
      }

      is Result.Error   -> {
        emit(Result.Error(result.message))
      }

      is Result.Loading -> {
        emit(Result.Loading)
      }
    }
  }

  override suspend fun toggleFavoriteMovie(movieId : Int) : Result<String> {
    return when (val resultUserId = getUserId()) {
      is Result.Success -> {
        val userId = resultUserId.data
        val result = safeDatabaseCall {
          favoriteMovieDao.toggleFavoriteMovie(userId, movieId)
        }
        when (result) {
          is Result.Success -> {
            Result.Success("Success Toggle Favorite")
          }

          is Result.Error   -> {
            Result.Error(result.message)
          }

          is Result.Loading -> {
            Result.Loading
          }
        }
      }

      is Result.Error   -> {
        Result.Error(resultUserId.message)
      }

      is Result.Loading -> {
        Result.Loading
      }


    }
  }


  private suspend fun getUserId() : Result<Long> {
    val userData = userPreferences.getUserData().firstOrNull()
    return userData?.userId?.let {
      Result.Success(it)
    } ?: Result.Error("ErrorMessages.UNAUTHORIZED")
  }


}