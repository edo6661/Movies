package com.example.submissionexpert1.data.db.dao.relation

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.relation.MovieGenreCrossRef
import com.example.submissionexpert1.data.db.entity.relation.MovieWithGenresEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieGenreDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGenreMovieCrossRef(genreMovieCrossRef : MovieGenreCrossRef)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGenresMoviesCrossRef(genreMoviesCrossRef : List<MovieGenreCrossRef>)

  @Transaction
  @Query(
    """
  SELECT movies.*, genres.*
  FROM movies
  INNER JOIN movie_genre_cross_ref ON movies.movieId = movie_genre_cross_ref.movieId
  INNER JOIN genres ON movie_genre_cross_ref.genreId = genres.genreId
  WHERE movies.movieId = :id
"""
  )
  fun getMovieWithGenresById(id : Int) : Flow<MovieWithGenresEntity?>

}