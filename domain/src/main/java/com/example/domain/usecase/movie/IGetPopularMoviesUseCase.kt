package com.example.domain.usecase.movie

import com.example.domain.common.Result
import com.example.domain.model.PaginationMovie
import kotlinx.coroutines.flow.Flow

interface IGetPopularMoviesUseCase {

  operator fun invoke(
    page : String,
  ) : Flow<Result<PaginationMovie>>
}