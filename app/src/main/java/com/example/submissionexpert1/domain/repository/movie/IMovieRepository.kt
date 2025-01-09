package com.example.submissionexpert1.domain.repository.movie

import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.domain.model.PaginationMovie
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {

  fun getPopularMovies(
    page : String,
  ) : Flow<Result<PaginationMovie>>

  fun getPopularMoviesFavorite(
    page : String,
  ) : Flow<Result<PaginationMovie>>

  fun getMoviesWithQuery(
    page : String,
    query : String,
  ) : Flow<Result<PaginationMovie>>

  fun getMovie(
    id : Int,
  ) : Flow<Result<Movie>>


  suspend fun toggleFavoriteMovie(
    movieId : Int,
  ) : Result<String>


}