package com.example.submissionexpert1.data.db.dao.relation

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.relation.PaginationFavoriteMovieEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationWithMovieAndFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMoviePaginationDao {

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
  FROM pagination as p
    INNER JOIN pagination_favorite_movies as pfm ON p.page = pfm.page
  INNER JOIN movies as m ON pfm.movieId = m.movieId
  AND EXISTS(
      SELECT 1 
      FROM pagination_favorite_movies as pfm
      WHERE pfm.movieId = m.movieId 
      AND pfm.userId = :userId
      AND pfm.page = :page
  )
  ORDER BY m.popularity DESC
  """
  )
  fun getFavoriteMoviesByUser(
    page : Int,
    userId : Long
  ) : Flow<List<PaginationWithMovieAndFavorite>>


  @Transaction
  suspend fun toggleFavoriteMovie(
    userId : Long,
    movieId : Int
  ) {
    val currentPage = 1
    var targetPage = currentPage

    val isFavorite = isMovieFavorite(userId, movieId)

    if (isFavorite) {
      deleteFavoriteMovie(userId, movieId)
    } else {
      while (true) {
        val favoriteCount = countFavoritesInPage(userId, targetPage)
        if (favoriteCount < 20) {
          break
        }
        targetPage ++
      }
      insertFavoriteMovie(
        PaginationFavoriteMovieEntity(
          page = targetPage,
          userId = userId,
          movieId = movieId,
          createdAt = System.currentTimeMillis()
        )
      )
    }
  }


  @Query(
    """
        SELECT EXISTS(
            SELECT 1 FROM pagination_favorite_movies 
            WHERE userId = :userId AND movieId = :movieId
        )
        """
  )
  suspend fun isMovieFavorite(userId : Long?, movieId : Int) : Boolean

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavoriteMovie(favoriteMovie : PaginationFavoriteMovieEntity)

  @Query(
    """
        DELETE FROM pagination_favorite_movies 
        WHERE userId = :userId AND movieId = :movieId
        """
  )
  suspend fun deleteFavoriteMovie(userId : Long, movieId : Int)

  @Query(
    """
    SELECT COUNT(*) 
    FROM pagination_favorite_movies 
    WHERE userId = :userId AND page = :page
    """
  )
  suspend fun countFavoritesInPage(userId : Long, page : Int) : Int


}
