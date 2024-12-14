package com.example.submissionexpert1.domain.usecase.movie

import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.PaginationMovie
import kotlinx.coroutines.flow.Flow

interface IGetPopularMoviesUseCase {

  operator fun invoke() : Flow<Result<PaginationMovie>>
}