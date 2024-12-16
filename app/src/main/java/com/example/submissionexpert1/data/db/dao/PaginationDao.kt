package com.example.submissionexpert1.data.db.dao

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationMovieEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationMovieRaw
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
  @Query(
    """
    SELECT pagination.*, movies.* 
    FROM pagination 
    INNER JOIN pagination_movies ON pagination.page = pagination_movies.page
    INNER JOIN movies ON pagination_movies.movieId = movies.movieId
    WHERE pagination.page = :page
    ORDER BY movies.popularity DESC
    """
  )
  fun getPaginationWithMoviesRaw(page : Int) : Flow<List<PaginationMovieRaw>>


}