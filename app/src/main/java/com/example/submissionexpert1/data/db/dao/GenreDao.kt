package com.example.submissionexpert1.data.db.dao

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.GenreEntity
import com.example.submissionexpert1.data.db.entity.relation.GenreWithMovies
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGenre(genre : GenreEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGenres(genres : List<GenreEntity>)

  @Query("SELECT * FROM genres WHERE genreId = :id")
  fun getGenreById(id : Int) : Flow<GenreEntity?>

  @Query("SELECT * FROM genres")
  fun getAllGenres() : Flow<List<GenreEntity>>

  @Transaction
  @Query("SELECT * FROM genres WHERE genreId = :genreId")
  fun getGenreWithMovies(genreId : Int) : Flow<GenreWithMovies>

}