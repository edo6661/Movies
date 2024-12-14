package com.example.submissionexpert1.data.repository.impl

import com.example.submissionexpert1.data.api.ApiService
import com.example.submissionexpert1.data.helper.mapper.toDomain
import com.example.submissionexpert1.data.repository.BaseRepository
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.PaginationMovie
import com.example.submissionexpert1.domain.repository.movie.IMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
  private val apiService : ApiService,
) : IMovieRepository, BaseRepository() {

  override fun getPopularMovies(
    page : String,
  ) : Flow<Result<PaginationMovie>> = flow {
    val popularMoviesResult = safeApiCall {
      apiService.getPopularMovies(
        page = page,
        // TODO: replace dengan dynamic language (pake shared preference / data store)
        language = "en-US",

        )
    }
    emit(popularMoviesResult.mapSuccessResult { it.toDomain() })
  }


}