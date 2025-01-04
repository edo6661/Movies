package com.example.submissionexpert1.application.di

import android.content.Context
import androidx.room.Room
import com.example.submissionexpert1.data.db.EntertainmentDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

  @Provides
  @Singleton
  fun provideDatabase(
    @ApplicationContext context : Context,
  ) : EntertainmentDb = Room.databaseBuilder(
    context,
    EntertainmentDb::class.java,
    EntertainmentDb.Constants.DATABASE_NAME
  )
    .build()

  @Provides
  @Singleton
  fun provideUserDao(db : EntertainmentDb) = db.userDao()

  @Provides
  @Singleton
  fun provideMovieDao(db : EntertainmentDb) = db.movieDao()

  @Provides
  @Singleton
  fun provideGenreDao(db : EntertainmentDb) = db.genreDao()

  @Provides
  @Singleton
  fun providePaginationDao(db : EntertainmentDb) = db.paginationDao()

  @Provides
  @Singleton
  fun provideMoviePaginationDao(db : EntertainmentDb) = db.moviePaginationDao()
}