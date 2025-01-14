package com.example.submissionexpert1.data.repository.impl.movie

import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.data.api.ApiService
import com.example.submissionexpert1.data.db.dao.MovieDao
import com.example.submissionexpert1.data.db.dao.PaginationDao
import com.example.submissionexpert1.data.db.dao.relation.FavoriteMoviePaginationDao
import com.example.submissionexpert1.data.db.dao.relation.MovieGenreDao
import com.example.submissionexpert1.data.db.dao.relation.MoviePaginationDao
import com.example.submissionexpert1.data.db.entity.relation.MovieGenreCrossRef
import com.example.submissionexpert1.data.helper.mapper.toDatabaseEntities
import com.example.submissionexpert1.data.helper.mapper.toDomain
import com.example.submissionexpert1.data.helper.mapper.toDomainWithFavorite
import com.example.submissionexpert1.data.helper.mapper.toEntity
import com.example.submissionexpert1.data.repository.BaseRepository
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import com.example.submissionexpert1.data.source.remote.response.PaginationMovieResponse
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.MovieWithGenres
import com.example.submissionexpert1.domain.model.PaginationMovie
import com.example.submissionexpert1.domain.repository.movie.IMovieRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
  private val apiService : ApiService,
  private val paginationDao : PaginationDao,
  private val moviePaginationDao : MoviePaginationDao,
  private val favoriteMovieDao : FavoriteMoviePaginationDao,
  private val movieDao : MovieDao,
  private val userPreferences : UserPreferences,
  private val movieGenreDao : MovieGenreDao
) : IMovieRepository, BaseRepository() {

  private val _favoriteChanges = MutableSharedFlow<FavoriteChangeEvent>()

  override val favoriteChanges = _favoriteChanges.asSharedFlow()


  override fun getPopularMovies(
    page : String,
  ) : Flow<Result<PaginationMovie>> = flow {


    val apiResult = safeApiCall {
      apiService.getPopularMovies(
        page = page,
        language = "en-US",
      )
    }
    emitAll(handleApiResultPopularMovies(apiResult, page))

  }


  private suspend fun handleApiResultPopularMovies(
    result : Result<PaginationMovieResponse>,
    page : String
  ) : Flow<Result<PaginationMovie>> = flow {
    when (result) {
      is Result.Success -> {
        val isPaginationExist = paginationDao.getPagination(page.toInt()).firstOrNull() != null
        if (! isPaginationExist) {
          insertAllToDao(result.data)
        }
        val cachedData = getCachedPopularMovies(page.toInt())
        emit(cachedData)
      }

      is Result.Error   -> {
        val message = result.message
        // ! nge handle unknown host exception
        if (message == ErrorMessages.NO_INTERNET_CONNECTION_ONLY_CACHE) {
          emitAll(getCachedPopularMoviesWhenError(page, result))
        } else {
          emit(Result.Error(result.message))
        }
      }


    }
  }

  private fun getCachedPopularMoviesWhenError(
    page : String,
    result : Result.Error
  ) : Flow<Result<PaginationMovie>> = flow {
    val cachedData = getCachedPopularMovies(page.toInt())
    val isDataNotNullAndFirstPage = cachedData !is Result.Error && page.toInt() == 1
    emit(cachedData)
    if (isDataNotNullAndFirstPage) {
      emit(Result.Error(result.message))
    }
  }


  private suspend fun getCachedPopularMovies(page : Int) : Result<PaginationMovie> {
    val userId = when (val resultUserId = getUserId()) {
      is Result.Success -> resultUserId.data
      is Result.Error   -> return Result.Error(resultUserId.message)
      null              -> null
    }
    val result = safeDatabaseCall {
      paginationDao.getPaginationWithMovies(page, userId ?: 0)
        .firstOrNull()
        ?.toDomainWithFavorite()
    }

    return when (result) {
      is Result.Success -> {
        result.data?.let {
          insertGenresMoviesCrossRef(it.results.flatMap { movie ->
            movie.genreIds?.map { genreId ->
              MovieGenreCrossRef(
                movieId = movie.id,
                genreId = genreId
              )
            } ?: emptyList()
          })
          Result.Success(it)
          // ! ini case nya padahal data cache nya itu null tapi kok error nya no internet connection? karena dia nge handle UnknownHostException, jadi cuman nerusin aja
        } ?: Result.Error(ErrorMessages.NO_INTERNET_CONNECTION)
      }

      is Result.Error   -> Result.Error(result.message)
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
    val userId = when (val resultUserId = getUserId()) {
      is Result.Success -> resultUserId.data

      is Result.Error   -> {
        emit(Result.Error(resultUserId.message))
        return@flow
      }


      null              -> {
        emit(Result.Error(ErrorMessages.UNAUTHORIZED))
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

    }
  }

  override fun getMoviesWithQuery(page : String, query : String) : Flow<Result<PaginationMovie>> =
    flow {
      val apiResult = safeApiCall {
        apiService.getMoviesWithQuery(
          page = page,
          query = query,
        )
      }
      when (apiResult) {
        is Result.Success -> {
          safeDatabaseCall {
            movieDao.insertMovies(apiResult.data.results.map { it.toEntity() })
          }
          insertGenresMoviesCrossRef(apiResult.data.results.flatMap { movie ->
            movie.genreIds?.map { genreId ->
              MovieGenreCrossRef(
                movieId = movie.id,
                genreId = genreId
              )
            } ?: emptyList()
          })
          emit(Result.Success(apiResult.data.toDomain()))

        }

        is Result.Error   -> {
          emit(Result.Error(apiResult.message))
        }

      }

    }

  override fun getMovie(id : Int) : Flow<Result<MovieWithGenres>> = flow {
    val userId = when (val resultUserId = getUserId()) {
      is Result.Success -> resultUserId.data

      is Result.Error   -> {
        emit(Result.Error(resultUserId.message))
        return@flow
      }


      null              -> {
        null
      }
    }


    val isMovieFavorite = favoriteMovieDao.isMovieFavorite(
      userId = userId ?: 0,
      movieId = id
    )
    val result = safeDatabaseCall {
      movieGenreDao.getMovieWithGenresById(id)
        .firstOrNull()
        ?.toDomain()

    }

    when (result) {
      is Result.Success -> {
        result.data?.let {
          it.movie.genreIds?.map { genreId ->
            insertGenreMovieCrossRef(
              MovieGenreCrossRef(
                movieId = it.movie.id,
                genreId = genreId
              )
            )
          } ?: emptyList()
          emit(
            Result.Success(
              it.copy(
                movie = it.movie.copy(
                  isFavorite = isMovieFavorite
                )
              )
            )
          )
        } ?: emit(
          Result.Error("Data tidak ada di database local")
        )
      }

      is Result.Error   -> {
        emit(Result.Error(result.message))
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
            _favoriteChanges.emit(FavoriteChangeEvent.Toggled(movieId))
            Result.Success("Success Toggle Favorite")
          }

          is Result.Error   -> {
            Result.Error(result.message)
          }


        }
      }

      is Result.Error   -> {
        Result.Error(resultUserId.message)
      }


      null              -> Result.Error(ErrorMessages.UNAUTHORIZED)
    }
  }


  private suspend fun getUserId() : Result<Long>? {

    val userData = userPreferences.getUserData().firstOrNull()
    return userData?.userId?.let {
      Result.Success(it)
    }
  }

  private suspend fun insertGenresMoviesCrossRef(genreMoviesCrossRef : List<MovieGenreCrossRef>) {
    safeDatabaseCall {
      movieGenreDao.insertGenresMoviesCrossRef(genreMoviesCrossRef)
    }
  }

  private suspend fun insertGenreMovieCrossRef(genreMovieCrossRef : MovieGenreCrossRef) {
    safeDatabaseCall {
      movieGenreDao.insertGenreMovieCrossRef(genreMovieCrossRef)
    }
  }


}

sealed class FavoriteChangeEvent {
  data class Toggled(val movieId : Int) : FavoriteChangeEvent()
}
