package com.example.domain.usecase.movie

import com.example.domain.common.Result
import com.example.domain.model.MovieWithGenres
import kotlinx.coroutines.flow.Flow

interface IGetMovieUseCase {

  operator fun invoke(id : Int) : Flow<Result<MovieWithGenres>>
}