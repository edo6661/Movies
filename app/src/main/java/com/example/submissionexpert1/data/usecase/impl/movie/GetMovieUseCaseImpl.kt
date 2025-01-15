package com.example.submissionexpert1.data.usecase.impl.movie

import com.example.domain.common.Result
import com.example.domain.model.MovieWithGenres
import com.example.domain.repository.movie.IMovieRepository
import com.example.domain.usecase.movie.IGetMovieUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieUseCaseImpl @Inject constructor(
  private val repo : IMovieRepository
) : IGetMovieUseCase {

  override fun invoke(id : Int) : Flow<Result<MovieWithGenres>> {
    return repo.getMovie(id)
  }

}