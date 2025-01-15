package com.example.submissionexpert1.application.di

import com.example.submissionexpert1.data.usecase.impl.movie.*
import com.example.submissionexpert1.data.usecase.impl.user.AuthUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

  @Provides
  fun provideGetPopularMoviesUseCase(
    movieRepository : com.example.domain.repository.movie.IMovieRepository
  ) : com.example.domain.usecase.movie.IGetPopularMoviesUseCase {
    return GetPopularMoviesUseCaseImpl(movieRepository)
  }

  @Provides
  fun provideAuthUseCase(
    authRepository : com.example.domain.repository.user.IAuthRepository
  ) : com.example.domain.usecase.user.IAuthUseCase {
    return AuthUseCaseImpl(authRepository)
  }

  @Provides
  fun provideToggleFavoriteMovieUseCase(
    movieRepository : com.example.domain.repository.movie.IMovieRepository
  ) : com.example.domain.usecase.movie.IToggleFavoriteMovieUseCase {
    return ToggleFavoriteMovieUseCaseImpl(movieRepository)

  }

  @Provides
  fun provideGetPopularMoviesFavoriteUseCase(
    movieRepository : com.example.domain.repository.movie.IMovieRepository
  ) : com.example.domain.usecase.movie.IGetPopularMoviesFavoriteUseCase {
    return GetPopularMoviesFavoriteUseCaseImpl(movieRepository)
  }

  @Provides
  fun provideGetMovieUseCase(
    movieRepository : com.example.domain.repository.movie.IMovieRepository
  ) : com.example.domain.usecase.movie.IGetMovieUseCase {
    return GetMovieUseCaseImpl(movieRepository)
  }

  @Provides
  fun provideGetMoviesWithQueryUseCase(
    movieRepository : com.example.domain.repository.movie.IMovieRepository
  ) : com.example.domain.usecase.movie.IGetMoviesWithQueryUseCase {
    return GetMoviesWithQueryUseCaseImpl(movieRepository)
  }

}
