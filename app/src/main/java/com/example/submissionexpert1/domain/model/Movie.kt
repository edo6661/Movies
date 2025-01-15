package com.example.submissionexpert1.domain.model

data class PaginationMovie(
  val page : Int,
  val totalResults : Int,
  val totalPages : Int,
  val results : List<Movie>
)

data class Movie(
  val overview : String?,
  val originalLanguage : String?,
  val originalTitle : String?,
  val video : Boolean?,
  val title : String?,
  val genreIds : List<Int>?,
  val posterPath : String?,
  val backdropPath : String?,
  val releaseDate : String?,
  val popularity : Double?,
  val voteAverage : Double?,
  val id : Int,
  val adult : Boolean?,
  val voteCount : Int?,
  val isFavorite : Boolean = false

)


data class MovieWithGenres(
  val movie : Movie,
  val genres : List<Genre>
)