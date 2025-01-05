package com.example.submissionexpert1.data.db

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.submissionexpert1.data.db.dao.*
import com.example.submissionexpert1.data.db.dao.relation.FavoriteMoviePaginationDao
import com.example.submissionexpert1.data.db.dao.relation.MoviePaginationDao
import com.example.submissionexpert1.data.db.entity.GenreEntity
import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.UserEntity
import com.example.submissionexpert1.data.db.entity.relation.MovieGenreCrossRef
import com.example.submissionexpert1.data.db.entity.relation.PaginationFavoriteMovieEntity
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
    MovieGenreCrossRef::class,
    PaginationEntity::class,
    PaginationMovieEntity::class,
    PaginationFavoriteMovieEntity::class

  ],
  version = 4,
  exportSchema = true,
  autoMigrations = [
    AutoMigration(
      from = 3,
      to = 4,
      spec = EntertainmentDb.MyAutoMigration::class
    )
  ]
)
abstract class EntertainmentDb : RoomDatabase() {

  object Constants {

    const val DATABASE_NAME = "entertainment_db"
  }

  @DeleteColumn(
    tableName = "movies",
    columnName = "favoriteUserIds"
  )
  class MyAutoMigration : AutoMigrationSpec

  abstract fun userDao() : UserDao
  abstract fun movieDao() : MovieDao
  abstract fun genreDao() : GenreDao
  abstract fun paginationDao() : PaginationDao
  abstract fun moviePaginationDao() : MoviePaginationDao
  abstract fun authDao() : AuthDao
  abstract fun favoriteMoviePaginationDao() : FavoriteMoviePaginationDao

  fun clearDatabase() {
    this.clearAllTables()
  }

}
