package com.example.submissionexpert1.data.db.dao.relation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationMovieEntity

@Dao
interface MoviePaginationDao {

  @Transaction
  suspend fun insertMoviesWithPagination(
    pagination : PaginationEntity,
    movies : List<MovieEntity>,
    paginationMovies : List<PaginationMovieEntity>
  ) {
    insertPaginationBatch(pagination)
    insertMoviesBatch(movies)
    insertPaginationMoviesBatch(paginationMovies)
  }

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPaginationBatch(pagination : PaginationEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMoviesBatch(movies : List<MovieEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPaginationMoviesBatch(paginationMovies : List<PaginationMovieEntity>)


}