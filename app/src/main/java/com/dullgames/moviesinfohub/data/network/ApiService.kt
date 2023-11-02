package com.dullgames.moviesinfohub.data.network

import com.dullgames.moviesinfohub.data.models.MovieCast
import com.dullgames.moviesinfohub.data.models.MovieDetail
import com.dullgames.moviesinfohub.data.models.Movies
import com.dullgames.moviesinfohub.data.models.TVSeriesResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/movie")
    suspend fun getMovieList(@Query("api_key") apiKey:String, @Query("query") query: String): Response<Movies>

    @GET("movie/now_playing")
    suspend fun getTrendingMovies(@Query("api_key") apiKey:String): Response<Movies>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey:String): Response<Movies>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey:String): Response<Movies>

    @GET("tv/airing_today")
    suspend fun getTrendingTVSeries(@Query("api_key")  apiKey:String): Response<TVSeriesResult>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") filmId: Int?,
        @Query("api_key") apiKey: String
    ):Response<MovieCast>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") filmId: Int?,
        @Query("api_key") apiKey: String
    ):Response<MovieDetail>

}