package com.example.submissionexpert1.data.db

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.submissionexpert1.data.db.dao.GenreDao
import com.example.submissionexpert1.data.db.dao.MovieDao
import com.example.submissionexpert1.data.db.dao.PaginationDao
import com.example.submissionexpert1.data.db.dao.UserDao
import com.example.submissionexpert1.data.db.dao.relation.MoviePaginationDao
import com.example.submissionexpert1.data.db.entity.GenreEntity
import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.UserEntity
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
    MovieGenreCrossRef::class,
    PaginationEntity::class,
    PaginationMovieEntity::class
  ],
  version = 2,
  exportSchema = true,
  autoMigrations = [
    AutoMigration(
      from = 1,
      to = 2,
      spec = EntertainmentDb.MyAutoMigration::class
    )
  ]
)
abstract class EntertainmentDb : RoomDatabase() {

  object Constants {

    const val DATABASE_NAME = "entertainment_db"
  }

  @RenameColumn(
    tableName = "movies",
    fromColumnName = "favoriteUserId",
    toColumnName = "favoriteUserIds"
  )
  class MyAutoMigration : AutoMigrationSpec

  abstract fun userDao() : UserDao
  abstract fun movieDao() : MovieDao
  abstract fun genreDao() : GenreDao
  abstract fun paginationDao() : PaginationDao
  abstract fun moviePaginationDao() : MoviePaginationDao

  fun clearDatabase() {
    this.clearAllTables()
  }

}