package com.example.submissionexpert1.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submissionexpert1.data.db.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMovie(movie : MovieEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMovies(movies : List<MovieEntity>)

  @Query("SELECT * FROM movies WHERE movieId = :id")
  fun getMovieById(id : Int) : Flow<MovieEntity?>

  @Query("SELECT * FROM movies")
  fun getAllMovies() : Flow<List<MovieEntity>>

}