package com.example.submissionexpert1.data.helper.mapper

import com.example.submissionexpert1.data.db.entity.GenreEntity
import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.relation.*
import com.example.submissionexpert1.data.source.remote.response.MovieResponse
import com.example.submissionexpert1.data.source.remote.response.PaginationMovieResponse
import com.example.domain.model.Genre
import com.example.domain.model.Movie
import com.example.domain.model.MovieWithGenres
import com.example.domain.model.PaginationMovie

fun PaginationMovieResponse.toDomain() : com.example.domain.model.PaginationMovie {
  return com.example.domain.model.PaginationMovie(
    page = page,
    results = results.map { it.toDomain() },
    totalPages = totalPages,
    totalResults = totalResults
  )
}

fun MovieResponse.toDomain() : com.example.domain.model.Movie {
  return com.example.domain.model.Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    adult = adult,
    video = video,
    genreIds = emptyList()
  )
}

fun com.example.domain.model.Movie.toEntity() : MovieEntity {
  return MovieEntity(
    movieId = id,
    title = title,
    overview = overview,
    posterPath = posterPath ?: "",
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    adult = adult,
    video = video,
    genreIds = genreIds
  )
}

fun PaginationMovieResponse.toPaginationEntity() : PaginationEntity {
  return PaginationEntity(
    page = page,
    totalPages = totalPages,

    totalResults = totalResults
  )
}

fun MovieResponse.toEntity() : MovieEntity {
  return MovieEntity(
    movieId = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    adult = adult,
    video = video,
    genreIds = genreIds,

    )
}


fun PaginationMovieResponse.toDatabaseEntities() : Triple<PaginationEntity, List<MovieEntity>, List<PaginationMovieEntity>> {
  val paginationEntity = this.toPaginationEntity()
  val movieEntities = this.results.map { it.toEntity() }
  val paginationMovieEntities = this.results.map {
    PaginationMovieEntity(page = this.page, movieId = it.id)
  }
  return Triple(paginationEntity, movieEntities, paginationMovieEntities)
}


fun MovieEntity.toDomain() : com.example.domain.model.Movie {
  return com.example.domain.model.Movie(
    id = movieId,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    adult = adult,
    video = video,
    genreIds = genreIds ?: emptyList(),
  )
}

fun MovieWithGenresEntity.toDomain() : com.example.domain.model.MovieWithGenres =
  com.example.domain.model.MovieWithGenres(
    movie = movie.toDomain(),
    genres = genres.map { it.toDomain() }
  )

fun GenreEntity.toDomain() : com.example.domain.model.Genre {
  return com.example.domain.model.Genre(
    genreId = genreId,
    name = name
  )
}

fun List<PaginationWithMovie>.toDomain() : com.example.domain.model.PaginationMovie {

  val paginationEntity = first().pagination

  val movies = this.map { it.movie.toDomain() }

  return com.example.domain.model.PaginationMovie(
    page = paginationEntity.page,
    results = movies,
    totalPages = paginationEntity.totalPages,
    totalResults = paginationEntity.totalResults
  )

}


fun MovieWithFavorite.toDomain() : com.example.domain.model.Movie {
  return com.example.domain.model.Movie(
    id = movie.movieId,
    title = movie.title,
    overview = movie.overview,
    posterPath = movie.posterPath,
    backdropPath = movie.backdropPath,
    releaseDate = movie.releaseDate,
    voteAverage = movie.voteAverage,
    voteCount = movie.voteCount,
    popularity = movie.popularity,
    originalLanguage = movie.originalLanguage,
    originalTitle = movie.originalTitle,
    adult = movie.adult,
    video = movie.video,
    genreIds = movie.genreIds ?: emptyList(),
    isFavorite = isFavorite
  )
}

fun List<PaginationWithMovieAndFavorite>.toDomainWithFavorite() : com.example.domain.model.PaginationMovie? {

  if (isEmpty()) return null

  val paginationEntity = first().pagination
  val movie = this.map { it.movie.toDomain() }

  return com.example.domain.model.PaginationMovie(
    page = paginationEntity.page,
    results = movie,
    totalPages = paginationEntity.totalPages,
    totalResults = paginationEntity.totalResults,
  )


}

