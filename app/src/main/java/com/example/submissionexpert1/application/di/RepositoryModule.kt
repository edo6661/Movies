package com.example.submissionexpert1.application.di

import com.example.domain.repository.movie.IMovieRepository
import com.example.domain.repository.user.IAuthRepository
import com.example.submissionexpert1.data.repository.impl.movie.MovieRepositoryImpl
import com.example.submissionexpert1.data.repository.impl.user.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  @Singleton
  abstract fun bindMovieRepository(movieRepositoryImpl : MovieRepositoryImpl) : IMovieRepository

  @Binds
  @Singleton
  abstract fun bindAuthRepository(authRepositoryImpl : AuthRepositoryImpl) : IAuthRepository


}