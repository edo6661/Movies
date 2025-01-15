package com.example.submissionexpert1.data.usecase.impl.movie

import com.example.domain.repository.movie.IMovieRepository
import com.example.domain.usecase.movie.IGetMoviesWithQueryUseCase
import javax.inject.Inject

class GetMoviesWithQueryUseCaseImpl @Inject constructor(
  private val repo : com.example.domain.repository.movie.IMovieRepository
) : com.example.domain.usecase.movie.IGetMoviesWithQueryUseCase {

  override operator fun invoke(page : String, query : String) = repo.getMoviesWithQuery(page, query)
}