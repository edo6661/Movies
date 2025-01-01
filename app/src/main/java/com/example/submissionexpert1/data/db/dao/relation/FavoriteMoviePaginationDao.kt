package com.example.submissionexpert1.data.db.dao.relation

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationFavoriteMovieEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationWithFavoriteMovies
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteMoviePaginationDao {


  @Transaction
  @Query(
    """
        SELECT pagination.* 
        FROM pagination 
        INNER JOIN pagination_favorite_movies as pfm ON pagination.page = pfm.page
        WHERE pfm.userId = :userId
        """
  )
  fun getPaginationWithFavoriteMoviesByUser(userId : Long) : Flow<List<PaginationWithFavoriteMovies>>

  @Transaction
  @Query(
    """
        SELECT movies.*
        FROM movies
        INNER JOIN pagination_favorite_movies as pfm ON movies.movieId = pfm.movieId
        WHERE pfm.userId = :userId
        ORDER BY movies.popularity DESC
        """
  )
  fun getFavoriteMoviesByUser(userId : Long) : Flow<List<MovieEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPaginationFavoriteMovies(paginationFavoriteMovies : List<PaginationFavoriteMovieEntity>)

  @Query(
    """
        DELETE FROM pagination_favorite_movies 
        WHERE userId = :userId AND movieId = :movieId
        """
  )
  suspend fun deleteFavoriteMovieByUser(userId : Long, movieId : Int)

  @Query(
    """
        DELETE FROM pagination_favorite_movies 
        WHERE userId = :userId
        """
  )
  suspend fun deleteAllFavoriteMoviesByUser(userId : Long)

}
