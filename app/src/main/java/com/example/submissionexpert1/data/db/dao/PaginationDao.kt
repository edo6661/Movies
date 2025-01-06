package com.example.submissionexpert1.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationMovieEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationWithMovieAndFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface PaginationDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPagination(pagination : PaginationEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPaginationMovies(paginationMovies : List<PaginationMovieEntity>)

  @Query("SELECT * FROM pagination where page = :page LIMIT 1")
  fun getPagination(page : Int) : Flow<PaginationEntity?>


  @Query(
    """
    SELECT 
        p.*,
        m.*,
        CASE 
            WHEN EXISTS(
                SELECT 1 
                FROM pagination_favorite_movies as pfm
                WHERE pfm.movieId = m.movieId 
                AND pfm.userId = :userId
            ) THEN 1 
            ELSE 0 
        END AS isFavorite
    FROM pagination  as p
    INNER JOIN pagination_movies as pm ON p.page = pm.page
    INNER JOIN movies as m ON pm.movieId = m.movieId
    WHERE p.page = :page
    ORDER BY m.popularity DESC
    """
  )
  fun getPaginationWithMovies(
    page : Int,
    userId : Long
  ) : Flow<List<PaginationWithMovieAndFavorite>>


}