package com.example.submissionexpert1.data.usecase.impl.movie

import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.PaginationMovie
import com.example.submissionexpert1.domain.repository.movie.IMovieRepository
import com.example.submissionexpert1.domain.usecase.movie.IGetPopularMoviesFavoriteUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularMoviesFavoriteUseCaseImpl @Inject constructor(
  private val movieRepository : IMovieRepository
) : IGetPopularMoviesFavoriteUseCase {

  override operator fun invoke(
    page : String,
  ) : Flow<Result<PaginationMovie>> {
    return movieRepository.getPopularMoviesFavorite(
      page,
    )
  }


}