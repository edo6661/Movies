package com.example.submissionexpert1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.submissionexpert1.data.db.dao.GenreDao
import com.example.submissionexpert1.data.db.dao.MovieDao
import com.example.submissionexpert1.data.db.dao.PaginationDao
import com.example.submissionexpert1.data.db.dao.UserDao
import com.example.submissionexpert1.data.db.entity.GenreEntity
import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.UserEntity
import com.example.submissionexpert1.data.db.entity.relation.FavoriteMovieEntity
import com.example.submissionexpert1.data.db.entity.relation.MovieGenreCrossRef
import com.example.submissionexpert1.data.db.entity.relation.PaginationMovieEntity
import com.example.submissionexpert1.data.db.helper.Converters

@TypeConverters(
  value = [
    Converters::class
  ]
)
@Database(
  entities = [
    UserEntity::class,
    MovieEntity::class,
    GenreEntity::class,
    FavoriteMovieEntity::class,
    MovieGenreCrossRef::class,
    PaginationEntity::class,
    PaginationMovieEntity::class
  ],
  version = 1,
  exportSchema = true,
//  autoMigrations = [
//    AutoMigration(from = 1, to = 2)
//  ]
)
abstract class EntertainmentDb : RoomDatabase() {

  object Constants {

    const val DATABASE_NAME = "entertainment_db"
  }

  abstract fun userDao() : UserDao
  abstract fun movieDao() : MovieDao
  abstract fun genreDao() : GenreDao
  abstract fun paginationDao() : PaginationDao
}