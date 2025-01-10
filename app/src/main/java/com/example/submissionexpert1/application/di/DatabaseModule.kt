package com.example.submissionexpert1.application.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.submissionexpert1.BuildConfig
import com.example.submissionexpert1.data.db.EntertainmentDb
import com.example.submissionexpert1.data.db.entity.GenreEntity
import com.example.submissionexpert1.data.source.local.preferences.SharedPreferencesHelper
import com.example.submissionexpert1.data.source.remote.response.GenreResponse
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

  private lateinit var INSTANCE : EntertainmentDb

  @Provides
  @Singleton
  fun provideDatabase(
    @ApplicationContext context : Context,
  ) : EntertainmentDb {
    val sharedPreferencesHelper = SharedPreferencesHelper(context)

    // Reset shared preferences untuk memaksa import ulang
    if (BuildConfig.DEBUG) {
      sharedPreferencesHelper.clearDataImportedFlag()
    }

    return Room.databaseBuilder(
      context,
      EntertainmentDb::class.java,
      EntertainmentDb.Constants.DATABASE_NAME
    )
      .fallbackToDestructiveMigration() // Ini akan menghapus database lama
      .addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db : SupportSQLiteDatabase) {
          super.onCreate(db)

          GlobalScope.launch(Dispatchers.IO) {
            try {
              if (! sharedPreferencesHelper.isDataImported()) {
                val genres = loadGenresFromJson(context)

                INSTANCE.genreDao().insertGenres(genres)

                sharedPreferencesHelper.setDataImported()
              }
            } catch (e : Exception) {
              e.printStackTrace()
            }
          }
        }
      })
      .build()
  }


  private fun loadGenresFromJson(context : Context) : List<GenreEntity> {
    return try {
      val jsonString = context.assets.open("genres.json").bufferedReader().use { it.readText() }

      val genreResponse = Gson().fromJson(jsonString, GenreResponse::class.java)

      genreResponse.genres
    } catch (e : Exception) {
      throw e
    }
  }


  @Provides
  @Singleton
  fun provideUserDao(db : EntertainmentDb) = db.userDao()

  @Provides
  @Singleton
  fun provideAuthDao(db : EntertainmentDb) = db.authDao()

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

  @Provides
  @Singleton
  fun provideFavoriteMoviePaginationDao(db : EntertainmentDb) = db.favoriteMoviePaginationDao()


}
