package com.example.submissionexpert1.domain.repository.movie

import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.PaginationMovie
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {

  fun getPopularMovies(
    page : String,
  ) : Flow<Result<PaginationMovie>>

  fun getPopularMoviesFavorite(
    page : String,
  ) : Flow<Result<PaginationMovie>>

  suspend fun toggleFavoriteMovie(
    movieId : Int,
  ) : Result<String>


}