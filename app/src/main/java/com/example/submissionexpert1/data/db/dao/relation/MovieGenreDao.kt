package com.example.submissionexpert1.data.db.dao.relation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submissionexpert1.data.db.entity.relation.MovieGenreCrossRef
import com.example.submissionexpert1.data.db.entity.relation.MovieWithGenres
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieGenreDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGenreMovieCrossRef(genreMovieCrossRef : MovieGenreCrossRef)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGenresMoviesCrossRef(genreMoviesCrossRef : List<MovieGenreCrossRef>)

  @Query(
    """
  SELECT movies.*, genres.*
  FROM movies
  INNER JOIN movie_genre_cross_ref ON movies.movieId = movie_genre_cross_ref.movieId
  INNER JOIN genres ON movie_genre_cross_ref.genreId = genres.genreId
  WHERE movies.movieId = :id
"""
  )
  fun getMovieById(id : Int) : Flow<MovieWithGenres?>

}