package com.example.submissionexpert1.data.usecase.impl.movie

import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.MovieWithGenres
import com.example.submissionexpert1.domain.repository.movie.IMovieRepository
import com.example.submissionexpert1.domain.usecase.movie.IGetMovieUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieUseCaseImpl @Inject constructor(
  private val repo : IMovieRepository
) : IGetMovieUseCase {

  override fun invoke(id : Int) : Flow<Result<MovieWithGenres>> {
    return repo.getMovie(id)
  }

}