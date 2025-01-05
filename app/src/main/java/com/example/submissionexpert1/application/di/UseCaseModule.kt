package com.example.submissionexpert1.application.di

import com.example.submissionexpert1.data.usecase.impl.movie.GetPopularMoviesUseCaseImpl
import com.example.submissionexpert1.data.usecase.impl.movie.ToggleFavoriteMovieUseCaseImpl
import com.example.submissionexpert1.data.usecase.impl.user.AuthUseCaseImpl
import com.example.submissionexpert1.domain.repository.movie.IMovieRepository
import com.example.submissionexpert1.domain.repository.user.IAuthRepository
import com.example.submissionexpert1.domain.usecase.movie.IGetPopularMoviesUseCase
import com.example.submissionexpert1.domain.usecase.movie.IToggleFavoriteMovieUseCase
import com.example.submissionexpert1.domain.usecase.user.IAuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

  @Provides
  fun provideGetPopularMoviesUseCase(
    movieRepository : IMovieRepository
  ) : IGetPopularMoviesUseCase {
    return GetPopularMoviesUseCaseImpl(movieRepository)
  }

  @Provides
  fun provideAuthUseCase(
    authRepository : IAuthRepository
  ) : IAuthUseCase {
    return AuthUseCaseImpl(authRepository)
  }

  @Provides
  fun provideToggleFavoriteMovieUseCase(
    movieRepository : IMovieRepository
  ) : IToggleFavoriteMovieUseCase {
    return ToggleFavoriteMovieUseCaseImpl(movieRepository)
  }

}
