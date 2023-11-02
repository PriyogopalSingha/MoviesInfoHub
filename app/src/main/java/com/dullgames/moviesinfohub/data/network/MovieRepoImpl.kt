package com.dullgames.moviesinfohub.data.network

import com.dullgames.moviesinfohub.API_KEY
import com.dullgames.moviesinfohub.ViewState
import com.dullgames.moviesinfohub.data.models.MovieCast
import com.dullgames.moviesinfohub.data.models.MovieDetail
import com.dullgames.moviesinfohub.data.models.Movies
import com.dullgames.moviesinfohub.data.models.TVSeriesResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MovieRepoImpl @Inject constructor(private val apiService: ApiService) {

    private val _moviesListFlow = MutableStateFlow<Movies>(Movies())
    val moviesListFlow : StateFlow<Movies>
        get() = _moviesListFlow

    private val _popularMoviesListFlow = MutableStateFlow<Movies>(Movies())
    val popularMoviesListFlow : StateFlow<Movies>
        get() = _popularMoviesListFlow

    private val _trendingMoviesListFlow = MutableStateFlow<Movies>(Movies())
    val trendingMoviesListFlow : StateFlow<Movies>
        get() = _trendingMoviesListFlow

    private val _topRatedMoviesListFlow = MutableStateFlow<Movies>(Movies())
    val topRatedMoviesListFlow : StateFlow<Movies>
        get() = _topRatedMoviesListFlow

    private val _tvseriesListFlow = MutableStateFlow<TVSeriesResult>(TVSeriesResult())
    val tvseriesListFlow : StateFlow<TVSeriesResult>
        get() = _tvseriesListFlow

    private val _movieDetailsFlow = MutableStateFlow<MovieDetail>(MovieDetail())
    val movieDetailsFlow : StateFlow<MovieDetail>
        get() = _movieDetailsFlow

    private val _movieCastFlow = MutableStateFlow<MovieCast>(MovieCast())
    val movieCastFlow : StateFlow<MovieCast>
        get() = _movieCastFlow


    suspend fun getMoviesList(query: String) {
        val response = apiService.getMovieList(API_KEY, query)
        if(response.body()!= null){
            _moviesListFlow.value = response.body()!!
        }
    }

    suspend fun getPopularMovies(){
        val response = apiService.getPopularMovies(API_KEY)
        if(response.body()!= null){
            _popularMoviesListFlow.value = response.body()!!
        }
    }

    suspend fun getTrendingMovies(){
        val response = apiService.getTrendingMovies(API_KEY)
        if(response.body()!= null){
            _trendingMoviesListFlow.value = response.body()!!
        }
    }


    suspend fun getTopRatedMovies(){
        val response = apiService.getTopRatedMovies(API_KEY)
        if(response.body()!= null){
            _topRatedMoviesListFlow.value = response.body()!!
        }
    }

    suspend fun getTrendingTVSeries(){
        val response = apiService.getTrendingTVSeries(API_KEY)
        if(response.body()!= null){
            _tvseriesListFlow.value = response.body()!!
        }
    }

    suspend fun getMovieDetails(id: Int?){
        val response = apiService.getMovieDetails(id, API_KEY)
        if(response.body()!= null){
            _movieDetailsFlow.value = response.body()!!
        }
    }
    suspend fun getMovieCast(id: Int?){
        val response = apiService.getMovieCast(id, API_KEY)
        if(response.body()!= null){
            _movieCastFlow.value = response.body()!!
        }
    }
}