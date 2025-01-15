package com.example.submissionexpert1.data.usecase.impl.movie

import com.example.domain.common.Result
import com.example.domain.repository.movie.IMovieRepository
import com.example.domain.usecase.movie.IToggleFavoriteMovieUseCase
import javax.inject.Inject

class ToggleFavoriteMovieUseCaseImpl @Inject constructor(
  private val movieRepository : com.example.domain.repository.movie.IMovieRepository
) : com.example.domain.usecase.movie.IToggleFavoriteMovieUseCase {

  override suspend fun invoke(movieId : Int) : com.example.domain.common.Result<String> {
    return movieRepository.toggleFavoriteMovie(movieId)
  }


}