package com.example.submissionexpert1.domain.repository.movie

import com.example.submissionexpert1.data.repository.impl.movie.FavoriteChangeEvent
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.MovieWithGenres
import com.example.submissionexpert1.domain.model.PaginationMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface IMovieRepository {

  private val _favoriteChanges : MutableSharedFlow<FavoriteChangeEvent>
    get() = MutableSharedFlow()
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
