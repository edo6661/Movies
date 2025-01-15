package com.example.submissionexpert1.domain.usecase.movie

import com.example.submissionexpert1.domain.common.Result


interface IToggleFavoriteMovieUseCase {

  suspend operator fun invoke(
    movieId : Int,
  ) : Result<String>
}