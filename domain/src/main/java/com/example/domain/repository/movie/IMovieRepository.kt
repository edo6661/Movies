package com.example.domain.repository.movie

import com.example.domain.common.Result
import com.example.domain.event.FavoriteChangeEvent
import com.example.domain.model.MovieWithGenres
import com.example.domain.model.PaginationMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface IMovieRepository {

  val favoriteChanges : SharedFlow<FavoriteChangeEvent>


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
  ) : Flow<Result<MovieWithGenres>>


  suspend fun toggleFavoriteMovie(
    movieId : Int,
  ) : Result<String>


}
