package com.example.submissionexpert1.data.db.entity.relation

import androidx.room.*
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

data class PaginationWithMovies(
  @Embedded val pagination : PaginationEntity,
  @Relation(
    parentColumn = "page",
    entityColumn = "movieId",
    associateBy = Junction(PaginationMovieEntity::class)
  )
  val movies : List<MovieEntity>

)

data class PaginationMovieRaw(
  @Embedded val pagination : PaginationEntity,
  @Embedded val movie : MovieEntity
)


