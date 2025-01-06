package com.example.submissionexpert1.data.db.entity.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
  val movieId : Int,
  @ColumnInfo(name = "created_at")
  val createdAt : Long? = null
)




