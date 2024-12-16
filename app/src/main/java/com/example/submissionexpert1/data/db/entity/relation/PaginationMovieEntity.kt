package com.example.submissionexpert1.data.db.entity.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.PaginationEntity

@Entity(
  tableName = "pagination_movies",
  primaryKeys = ["page", "movieId"],
  foreignKeys = [
    ForeignKey(
      entity = PaginationEntity::class,
      parentColumns = ["page"],
      childColumns = ["page"]
    ),
    ForeignKey(
      entity = MovieEntity::class,
      parentColumns = ["movieId"],
      childColumns = ["movieId"]
    )
  ],
  indices = [
    Index(value = ["page"]),
    Index(value = ["movieId"])
  ]
)
data class PaginationMovieEntity(
  val page : Int,
  val movieId : Int
)

data class PaginationWithMovie(
  @Embedded val pagination : PaginationEntity,
  @Embedded val movie : MovieEntity
)


