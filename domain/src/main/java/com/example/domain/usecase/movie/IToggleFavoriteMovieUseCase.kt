package com.example.domain.usecase.movie

import com.example.domain.common.Result


interface IToggleFavoriteMovieUseCase {

  suspend operator fun invoke(
    movieId : Int,
  ) : Result<String>
}