package com.example.submissionexpert1.data.db.entity.relation

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.UserEntity

@Entity(
  tableName = "pagination_favorite_movies",
  primaryKeys = ["page", "userId", "movieId"],
  foreignKeys = [
    ForeignKey(
      entity = PaginationEntity::class,
      parentColumns = ["page"],
      childColumns = ["page"]
    ),
    ForeignKey(
      entity = UserEntity::class,
      parentColumns = ["userId"],
      childColumns = ["userId"]
    ),
    ForeignKey(
      entity = MovieEntity::class,
      parentColumns = ["movieId"],
      childColumns = ["movieId"]
    )
  ],
  indices = [
    Index(value = ["page"]),
    Index(value = ["userId"]),
    Index(value = ["movieId"])
  ]
)
data class PaginationFavoriteMovieEntity(
  val page : Int,
  val userId : Long,
  val movieId : Int
)

data class PaginationWithFavoriteMovies(
  @Embedded val pagination : PaginationEntity,
  @Relation(
    parentColumn = "page",
    entityColumn = "movieId",
    associateBy = Junction(PaginationFavoriteMovieEntity::class)
  )
  val favoriteMovies : List<MovieEntity>
)
