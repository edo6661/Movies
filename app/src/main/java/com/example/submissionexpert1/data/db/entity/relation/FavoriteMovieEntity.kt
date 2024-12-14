package com.example.submissionexpert1.data.db.entity.relation

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.UserEntity

@Entity(
  tableName = "favorite_movies",
  primaryKeys = ["userId", "movieId"],
  foreignKeys = [
    ForeignKey(
      entity = UserEntity::class,
      parentColumns = ["userId"],
      childColumns = ["userId"],
    ),
    ForeignKey(
      entity = MovieEntity::class,
      parentColumns = ["movieId"],
      childColumns = ["movieId"]
    )
  ],
  indices = [
    Index(value = ["userId"]),
    Index(value = ["movieId"])
  ]
)
data class FavoriteMovieEntity(

  val userId : Long,
  val movieId : Int
)

data class UserWithFavoriteMovies(
  @Embedded val user : UserEntity,
  @Relation(
    parentColumn = "userId",
    entityColumn = "movieId",
    associateBy = Junction(FavoriteMovieEntity::class)
  )
  val favoriteMovies : List<MovieEntity>
)
