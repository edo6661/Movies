package com.example.submissionexpert1.domain.repository.movie

import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.PaginationMovie
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {

  fun getPopularMovies() : Flow<Result<PaginationMovie>>
}