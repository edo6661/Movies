package com.example.submissionexpert1.data.db.dao

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationMovieEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationWithMovies
import kotlinx.coroutines.flow.Flow

@Dao
interface PaginationDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPagination(pagination : PaginationEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPaginationMovies(paginationMovies : List<PaginationMovieEntity>)

  @Query("SELECT * FROM pagination where page = :page LIMIT 1")
  fun getPagination(page : Int) : Flow<PaginationEntity?>

  @Transaction
  @Query("SELECT * FROM pagination WHERE page = :page")
  suspend fun getPaginationWithMovies(page : Int) : PaginationWithMovies?


}