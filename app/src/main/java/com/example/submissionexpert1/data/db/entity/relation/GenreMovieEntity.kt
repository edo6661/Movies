package com.example.submissionexpert1.data.db.entity.relation

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.GenreEntity
import com.example.submissionexpert1.data.db.entity.MovieEntity

@Entity(
  tableName = "movie_genre_cross_ref",
  primaryKeys = ["movieId", "genreId"],
  foreignKeys = [
    ForeignKey(
      entity = MovieEntity::class,
      parentColumns = ["movieId"],
      childColumns = ["movieId"]
    ),
    ForeignKey(entity = GenreEntity::class, parentColumns = ["genreId"], childColumns = ["genreId"])
  ],
  indices = [
    Index(value = ["movieId"]),
    Index(value = ["genreId"])
  ]
)
data class MovieGenreCrossRef(
  val movieId : Int,
  val genreId : Int
)

data class MovieWithGenresEntity(
  @Embedded val movie : MovieEntity,
  @Relation(
    parentColumn = "movieId",
    entityColumn = "genreId",
    associateBy = Junction(MovieGenreCrossRef::class)
  )
  val genres : List<GenreEntity>
)

data class GenreWithMovies(
  @Embedded val genre : GenreEntity,
  @Relation(
    parentColumn = "genreId",
    entityColumn = "movieId",
    associateBy = Junction(MovieGenreCrossRef::class)
  )
  val movies : List<MovieEntity>
)



