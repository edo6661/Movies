package com.example.submissionexpert1.data.api

import com.example.submissionexpert1.data.source.remote.response.PaginationMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

  @GET(ApiConfig.POPULAR_MOVIE)
  suspend fun getPopularMovies(
    @Query("page") page : String = "1",
    @Query("language") language : String = "id-ID",

    ) : Response<PaginationMovieResponse>

  @GET(ApiConfig.TOP_RATED)
  suspend fun getTopRatedMovies(
    @Query("page") page : String = "1",
    @Query("language") language : String = "id-ID",
    @Query("sort_by") sortBy : String = "popularity.desc"
  ) : Response<PaginationMovieResponse>

}