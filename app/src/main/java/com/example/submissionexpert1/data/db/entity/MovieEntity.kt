package com.example.submissionexpert1.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
  @PrimaryKey val movieId : Int,
  val overview : String,
  val originalLanguage : String,
  val originalTitle : String,
  val video : Boolean,
  val title : String,
  val genreIds : List<Int>,
  val posterPath : String,
  val backdropPath : String?,
  val releaseDate : String,
  val popularity : Double,
  val voteAverage : Double,
  val adult : Boolean,
  val voteCount : Int,
)
