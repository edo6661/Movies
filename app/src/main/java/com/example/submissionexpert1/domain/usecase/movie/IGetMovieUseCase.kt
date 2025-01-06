package com.example.submissionexpert1.domain.usecase.movie

import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface IGetMovieUseCase {

  operator fun invoke(id : Int) : Flow<Result<Movie>>
}