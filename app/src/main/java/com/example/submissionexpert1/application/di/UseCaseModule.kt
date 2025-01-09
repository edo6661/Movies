package com.example.submissionexpert1.application.di

import com.example.submissionexpert1.data.usecase.impl.movie.*
import com.example.submissionexpert1.data.usecase.impl.user.AuthUseCaseImpl
import com.example.submissionexpert1.domain.repository.movie.IMovieRepository
import com.example.submissionexpert1.domain.repository.user.IAuthRepository
import com.example.submissionexpert1.domain.usecase.movie.*
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

  @Provides
  fun provideGetPopularMoviesFavoriteUseCase(
    movieRepository : IMovieRepository
  ) : IGetPopularMoviesFavoriteUseCase {
    return GetPopularMoviesFavoriteUseCaseImpl(movieRepository)
  }

  @Provides
  fun provideGetMovieUseCase(
    movieRepository : IMovieRepository
  ) : IGetMovieUseCase {
    return GetMovieUseCaseImpl(movieRepository)
  }

  @Provides
  fun provideGetMoviesWithQueryUseCase(
    movieRepository : IMovieRepository
  ) : IGetMoviesWithQueryUseCase {
    return GetMoviesWithQueryUseCaseImpl(movieRepository)
  }

}
