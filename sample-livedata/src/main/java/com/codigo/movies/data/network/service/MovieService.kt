package com.codigo.movies.data.network.service

import com.codigo.movies.domain.model.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/now_playing")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse
}

data class MovieResponse(
    val results: List<Movie> = emptyList()
)