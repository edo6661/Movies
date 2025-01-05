package com.example.submissionexpert1.data.db.dao.relation

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.relation.PaginationFavoriteMovieEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationWithMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMoviePaginationDao {

  @Transaction
  @Query(
    """
    SELECT pagination.*, movies.* 
    FROM pagination 
    INNER JOIN pagination_movies AS pm ON pagination.page = pm.page
    INNER JOIN movies ON pm.movieId = movies.movieId
    WHERE pagination.page = :page 
    AND movies.movieId IN (
      SELECT movieId 
      FROM pagination_favorite_movies 
      WHERE userId = :userId
    )
    ORDER BY movies.popularity DESC
    """
  )
  fun getPaginationWithFavoriteMovies(
    page : Int,
    userId : Long
  ) : Flow<List<PaginationWithMovie>>

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
          movieId = movieId
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
  suspend fun isMovieFavorite(userId : Long, movieId : Int) : Boolean

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
