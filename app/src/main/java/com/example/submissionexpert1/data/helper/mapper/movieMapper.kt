package com.example.submissionexpert1.data.helper.mapper

import com.example.submissionexpert1.data.db.entity.MovieEntity
import com.example.submissionexpert1.data.db.entity.PaginationEntity
import com.example.submissionexpert1.data.db.entity.relation.PaginationWithMovies
import com.example.submissionexpert1.data.source.remote.response.MovieResponse
import com.example.submissionexpert1.data.source.remote.response.PaginationMovieResponse
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.domain.model.PaginationMovie

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
    genreIds = genreIds
  )
}

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
    genreIds = genreIds
  )
}

fun PaginationWithMovies.toDomain() : PaginationMovie {
  return PaginationMovie(
    page = pagination.page,
    results = movies.map { it.toDomain() },
    totalPages = pagination.totalPages,
    totalResults = pagination.totalResults
  )
}
