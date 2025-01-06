package com.example.submissionexpert1.data.db.entity.relation

import androidx.room.Embedded
import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.PaginationEntity

data class MovieWithFavorite(
  @Embedded val movie : MovieEntity,
  val isFavorite : Boolean
)

data class PaginationWithMovieAndFavorite(
  @Embedded val pagination : PaginationEntity,
  @Embedded val movie : MovieWithFavorite
)