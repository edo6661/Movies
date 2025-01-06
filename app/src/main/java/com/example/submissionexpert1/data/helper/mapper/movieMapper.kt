package com.example.submissionexpert1.data.helper.mapper

import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.relation.MovieWithFavorite
import com.example.submissionexpert1.data.db.entity.relation.PaginationMovieEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationWithMovie
import com.example.submissionexpert1.data.db.entity.relation.PaginationWithMovieAndFavorite
import com.example.submissionexpert1.data.source.remote.response.MovieResponse
import com.example.submissionexpert1.data.source.remote.response.PaginationMovieResponse
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.domain.model.PaginationMovie

// ! RES
fun PaginationMovieResponse.toDomain() : PaginationMovie {
  return PaginationMovie(
    page = page,
    results = results.map { it.toDomain() },
    totalPages = totalPages,
    totalResults = totalResults
  )
}

fun MovieResponse.toDomain() : Movie {
  return Movie(
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

fun MovieResponse.toMovieEntity() : MovieEntity {
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
  val movieEntities = this.results.map { it.toMovieEntity() }
  val paginationMovieEntities = this.results.map {
    PaginationMovieEntity(page = this.page, movieId = it.id)
  }
  return Triple(paginationEntity, movieEntities, paginationMovieEntities)
}


// ! entity

fun MovieEntity.toDomain() : Movie {
  return Movie(
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
    genreIds = genreIds,
  )
}

fun List<PaginationWithMovie>.toDomain() : PaginationMovie {

  val paginationEntity = first().pagination

  val movies = this.map { it.movie.toDomain() }

  return PaginationMovie(
    page = paginationEntity.page,
    results = movies,
    totalPages = paginationEntity.totalPages,
    totalResults = paginationEntity.totalResults
  )

}


fun MovieWithFavorite.toDomain() : Movie {
  return Movie(
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
    genreIds = movie.genreIds,
    isFavorite = isFavorite
  )
}

fun List<PaginationWithMovieAndFavorite>.toDomainWithFavorite() : PaginationMovie? {
  
  if (isEmpty()) return null

  val paginationEntity = first().pagination
  val movie = this.map { it.movie.toDomain() }

  return PaginationMovie(
    page = paginationEntity.page,
    results = movie,
    totalPages = paginationEntity.totalPages,
    totalResults = paginationEntity.totalResults,
  )

}

