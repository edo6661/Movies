package com.example.submissionexpert1.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.submissionexpert1.data.db.dao.*
import com.example.submissionexpert1.data.db.dao.relation.FavoriteMoviePaginationDao
import com.example.submissionexpert1.data.db.dao.relation.MovieGenreDao
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
  version = 5,
  exportSchema = true,
  autoMigrations = [
    AutoMigration(
      from = 4,
      to = 5,
//      spec = EntertainmentDb.MyAutoMigration::class
    )
  ]
)
abstract class EntertainmentDb : RoomDatabase() {

  object Constants {

    const val DATABASE_NAME = "entertainment_db"
  }

//  @RenameColumn(
//    tableName = "pagination_favorite_movies",
//    fromColumnName = "",
//    toColumnName = "createdAt"
//  )
//  class MyAutoMigration : AutoMigrationSpec

  abstract fun userDao() : UserDao
  abstract fun movieDao() : MovieDao
  abstract fun genreDao() : GenreDao
  abstract fun paginationDao() : PaginationDao
  abstract fun moviePaginationDao() : MoviePaginationDao
  abstract fun authDao() : AuthDao
  abstract fun favoriteMoviePaginationDao() : FavoriteMoviePaginationDao
  abstract fun movieGenreDao() : MovieGenreDao

  fun clearDatabase() {
    this.clearAllTables()
  }

}
